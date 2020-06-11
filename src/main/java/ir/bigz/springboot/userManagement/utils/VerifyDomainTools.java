package ir.bigz.springboot.userManagement.utils;

import ir.bigz.springboot.userManagement.domain.UserApp;

import java.util.function.Predicate;

public interface VerifyDomainTools extends Predicate<UserApp> {

    static VerifyDomainTools isEmailVerify(int hashCode) {
        return userApp -> {
            if (userApp.getHashCode() == hashCode) {
                return true;
            } else {
                return false;
            }
        };
    }
}
