package ru.edu.pr08;

public class ClientCsvParser {
    public ClientDraft parse(String line, int lineNumber) {
        if (line == null) {
            throw new IllegalArgumentException("Строка " + lineNumber + ": строка отсутствует");
        }

        String[] parts = line.split(";", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Строка " + lineNumber + ": ожидается 3 поля, найдено " + parts.length);
        }

        return new ClientDraft(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        );
    }
}
