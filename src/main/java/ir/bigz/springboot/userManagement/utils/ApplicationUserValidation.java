package ir.bigz.springboot.userManagement.utils;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;

import java.util.function.Function;

import java.util.regex.Pattern;

import static ir.bigz.springboot.userManagement.utils.ApplicationUserValidation.ValidationResult.*;
import static ir.bigz.springboot.userManagement.utils.ApplicationUserValidation.*;

public interface ApplicationUserValidation extends Function<ApplicationUser, ValidationResult> {

    Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");


    static ApplicationUserValidation isUserNameNotNull() {
        return userApp ->
                userApp.getUserName() != null ?
                        SUCCESS : USERNAME_HAS_NULL;
    }

    static ApplicationUserValidation isPhoneNumberNotNull() {
        return userApp ->
                userApp.getPhoneNumber() != null ?
                        SUCCESS : PHONE_NUMBER_HAS_NULL;
    }

    static ApplicationUserValidation isEmailNotNull() {
        return userApp ->
                userApp.getEmail() != null ?
                        SUCCESS : EMAIL_HAS_NULL;
    }

    static ApplicationUserValidation isEmailValid() {
        return userApp ->
                pattern.matcher(userApp.getEmail()).find() ?
                        SUCCESS : EMAIL_NOT_VALID;
    }

    static ApplicationUserValidation isPhoneNumberValid() {
        return userApp -> userApp.getPhoneNumber().startsWith("0") &&
                userApp.getPhoneNumber().length() == 11 ?
                SUCCESS : PHONE_NUMBER_NOT_VALID;
    }

    static ApplicationUserValidation isQuestionAndAnswerNotEmpty() {
        return userApp -> userApp.getQuestionAndAnswerMap().size() > 0 ?
                SUCCESS : QUESTION_NOT_ANSWER;
    }

    default ApplicationUserValidation and (ApplicationUserValidation other) {
        return userApp -> {
            ApplicationUserValidation.ValidationResult result = this.apply(userApp);
            return result.equals(SUCCESS) ? other.apply(userApp) : result;
        };
    }

    enum ValidationResult{
        SUCCESS,
        USERNAME_HAS_NULL,
        EMAIL_HAS_NULL,
        PHONE_NUMBER_HAS_NULL,
        PHONE_NUMBER_NOT_VALID,
        EMAIL_NOT_VALID,
        QUESTION_NOT_ANSWER
    }
}
