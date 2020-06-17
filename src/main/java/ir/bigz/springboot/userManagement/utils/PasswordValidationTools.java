package ir.bigz.springboot.userManagement.utils;

import java.util.function.Predicate;

public interface PasswordValidationTools extends Predicate<String> {

    static PasswordValidationTools isTwoPasswordSame(String secondPassword) {
        return firstPassword -> {
            if (firstPassword.equals(secondPassword)) {
                return true;
            } else {
                return false;
            }
        };
    }
}
