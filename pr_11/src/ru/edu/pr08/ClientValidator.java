package ru.edu.pr08;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {
    public List<String> validate(ClientDraft d) {
        List<String> errors = new ArrayList<>();

        if (d == null) {
            errors.add("Черновик клиента отсутствует");
            return errors;
        }

        String inn = d.inn();
        if (inn == null || inn.isBlank()) {
            errors.add("ИНН пуст");
        } else if (!inn.matches("\\d{10}|\\d{12}")) {
            errors.add("ИНН должен содержать 10 или 12 цифр");
        }

        String email = d.email();
        if (email == null || email.isBlank()) {
            errors.add("Email пуст");
        } else {
            int firstAt = email.indexOf('@');
            int lastAt = email.lastIndexOf('@');
            if (firstAt <= 0 || firstAt != lastAt || firstAt == email.length() - 1) {
                errors.add("Email должен содержать ровно один @ и непустые части слева и справа");
            }
        }

        if (d.name() == null || d.name().isBlank()) {
            errors.add("Имя клиента пусто");
        }

        return errors;
    }
}
