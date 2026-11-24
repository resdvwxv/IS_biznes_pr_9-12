package ru.edu.pr08;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ClientImporter {
    private final ClientCsvParser parser = new ClientCsvParser();
    private final ClientValidator validator = new ClientValidator();
    private final ClientRepository repository;

    public ClientImporter(ClientRepository r) {
        repository = r;
    }

    public ImportResult importFile(Path path) throws IOException {
        ImportResult result = new ImportResult();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) {
                result.addError(new ImportError(0, "Файл пуст: отсутствует строка заголовка"));
                return result;
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    ClientDraft draft = parser.parse(line, lineNumber);
                    List<String> validationErrors = validator.validate(draft);

                    if (!validationErrors.isEmpty()) {
                        result.addError(new ImportError(
                                lineNumber,
                                String.join("; ", validationErrors)
                        ));
                        continue;
                    }

                    if (repository.findByInn(draft.inn()) != null) {
                        result.addError(new ImportError(
                                lineNumber,
                                "Дубликат ИНН: " + draft.inn()
                        ));
                        continue;
                    }

                    repository.save(new Client(draft.inn(), draft.name(), draft.email()));
                    result.addAccepted();

                } catch (IllegalArgumentException e) {
                    result.addError(new ImportError(lineNumber, e.getMessage()));
                }
            }
        }

        return result;
    }
}
