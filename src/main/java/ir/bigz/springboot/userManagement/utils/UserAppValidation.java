package ir.bigz.springboot.userManagement.utils;

import ir.bigz.springboot.userManagement.domain.UserApp;

import java.util.function.Function;
import java.util.regex.Pattern;

import static ir.bigz.springboot.userManagement.utils.UserAppValidation.ValidationResult.*;
import static ir.bigz.springboot.userManagement.utils.UserAppValidation.*;

public interface UserAppValidation extends Function<UserApp, ValidationResult> {

    static Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    static UserAppValidation isEmailValid() {
        return userApp ->
            pattern.matcher(userApp.getEmail()).find() ?
                    SUCCESS : EMAIL_NOT_VALID;
    }

    static UserAppValidation isPhoneNumberValid() {
        return userApp -> userApp.getPhoneNumber().startsWith("0") &&
                userApp.getPhoneNumber().length() == 11 ?
                SUCCESS : PHONE_NUMBER_NOT_VALID;
    }

    static UserAppValidation isEmailVerify(int hashCode) {
        return userApp -> userApp.hashCode() == hashCode ?
                SUCCESS : EMAIL_NOT_VERIFY;
    }

    default UserAppValidation and (UserAppValidation other) {
        return userApp -> {
            ValidationResult result = this.apply(userApp);
            return result.equals(SUCCESS) ? other.apply(userApp) : result;
        };
    }

    enum ValidationResult{
        SUCCESS,
        PHONE_NUMBER_NOT_VALID,
        EMAIL_NOT_VALID,
        EMAIL_NOT_VERIFY
    }
}
