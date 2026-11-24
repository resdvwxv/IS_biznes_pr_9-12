package ru.edu.pr08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class SelfCheck {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        ClientCsvParser parser = new ClientCsvParser();
        ClientValidator validator = new ClientValidator();

        check("parse valid line", () -> {
            ClientDraft d = parser.parse("7701234567;Alpha LLC;office@alpha.ru", 2);
            eq(d.inn(), "7701234567");
            eq(d.name(), "Alpha LLC");
            eq(d.email(), "office@alpha.ru");
        });

        check("parse wrong field count", () -> {
            expectThrows(IllegalArgumentException.class,
                    () -> parser.parse("7701234567;Alpha LLC", 2));
        });

        check("validate INN 10 digits accepted", () -> {
            eq(validator.validate(new ClientDraft("7701234567", "A", "a@b.ru")).size(), 0);
        });

        check("validate INN 12 digits accepted", () -> {
            eq(validator.validate(new ClientDraft("781234567890", "B", "b@c.ru")).size(), 0);
        });

        check("validate INN 9 digits rejected", () -> {
            checkContains(validator.validate(new ClientDraft("123456789", "C", "c@d.ru")),
                    "10 или 12");
        });

        check("validate INN 11 digits rejected", () -> {
            checkContains(validator.validate(new ClientDraft("12345678901", "C", "c@d.ru")),
                    "10 или 12");
        });

        check("validate INN non-digit rejected", () -> {
            checkContains(validator.validate(new ClientDraft("770123456A", "C", "c@d.ru")),
                    "10 или 12");
        });

        check("validate email exactly one @ accepted", () -> {
            eq(validator.validate(new ClientDraft("7701234567", "A", "left@right")).size(), 0);
        });

        check("validate email no @ rejected", () -> {
            checkContains(validator.validate(new ClientDraft("7701234567", "A", "wrongmail")),
                    "ровно один @");
        });

        check("validate email two @ rejected", () -> {
            checkContains(validator.validate(new ClientDraft("7701234567", "A", "a@@b")),
                    "ровно один @");
        });

        check("validate email empty left rejected", () -> {
            checkContains(validator.validate(new ClientDraft("7701234567", "A", "@right")),
                    "ровно один @");
        });

        check("validate email empty right rejected", () -> {
            checkContains(validator.validate(new ClientDraft("7701234567", "A", "left@")),
                    "ровно один @");
        });

        Path sample = Paths.get("data/sample.csv");
        check("import sample.csv accepted=2 errors=2", () -> {
            ClientRepository repo = new ClientRepository();
            ImportResult r = new ClientImporter(repo).importFile(sample);

            eq(r.accepted(), 2);
            eq(r.errors().size(), 2);
            eq(repo.findByInn("7701234567") != null, true);
            eq(repo.findByInn("781234567890") != null, true);
            eq(repo.findByInn("123") == null, true);
        });

        Path tmp = Files.createTempFile("clients-", ".csv");
        try {
            Files.writeString(tmp, """
                    inn;name;email
                    7701234567;First;first@mail.ru
                    7701234567;Duplicate;dup@mail.ru
                    781234567890;Second;second@mail.ru
                    """);

            check("duplicate INN rejected and first kept", () -> {
                ClientRepository repo = new ClientRepository();
                ImportResult r = new ClientImporter(repo).importFile(tmp);

                eq(r.accepted(), 2);
                eq(r.errors().size(), 1);
                eq(r.errors().get(0).message().contains("Дубликат"), true);
                eq(repo.findByInn("7701234567").name(), "First");
            });
        } finally {
            Files.deleteIfExists(tmp);
        }

        check("missing file throws IOException", () -> {
            expectThrows(IOException.class,
                    () -> new ClientImporter(new ClientRepository())
                            .importFile(Paths.get("data/does-not-exist.csv")));
        });

        System.out.println("PASSED=" + passed + " FAILED=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String name, CheckedRunnable action) {
        try {
            action.run();
            passed++;
            System.out.println("PASS " + name);
        } catch (Throwable t) {
            failed++;
            System.out.println("FAIL " + name + " -> " + t.getMessage());
        }
    }

    private static void eq(Object actual, Object expected) {
        if (!Objects.equals(actual, expected)) {
            throw new AssertionError("expected=" + expected + ", actual=" + actual);
        }
    }

    private static void checkContains(List<String> errors, String fragment) {
        if (errors.stream().noneMatch(e -> e.contains(fragment))) {
            throw new AssertionError("no error contains '" + fragment + "': " + errors);
        }
    }

    private static void expectThrows(Class<? extends Throwable> type, CheckedRunnable action) {
        try {
            action.run();
        } catch (Throwable t) {
            if (type.isInstance(t)) {
                return;
            }
            throw new AssertionError(
                    "expected " + type.getSimpleName()
                            + " but got " + t.getClass().getSimpleName(), t);
        }
        throw new AssertionError("expected " + type.getSimpleName() + " but nothing was thrown");
    }

    @FunctionalInterface
    private interface CheckedRunnable {
        void run() throws Exception;
    }
}
