package ir.bigz.springboot.userManagement.utils;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Consumer;

public interface ApplicationUserDataEntry extends Consumer<ApplicationUser> {

    static ApplicationUserDataEntry setAccountNonExpired(boolean status) {
        return userApp ->
                userApp.setAccountNonExpired(status);
    }

    static ApplicationUserDataEntry setAccountNonLocked(boolean status) {
        return userApp ->
                userApp.setAccountNonLocked(status);
    }

    static ApplicationUserDataEntry setCredentialsNonExpired(boolean status) {
        return userApp ->
                userApp.setCredentialsNonExpired(status);
    }

    static ApplicationUserDataEntry setEnabled(boolean status) {
        return userApp ->
                userApp.setEnabled(status);
    }

    static ApplicationUserDataEntry setJoinDate() {
        return userApp ->
                userApp.setJoinDate(DateAndTimeTools.getTimestampNow());
    }

    static ApplicationUserDataEntry setLastUpdateDate() {
        return userApp ->
                userApp.setLastUpdateDate(DateAndTimeTools.getTimestampNow());
    }

    static ApplicationUserDataEntry setPassword(PasswordEncoder passwordEncoder) {
        return userApp ->
                userApp.setPassword(passwordEncoder.encode(userApp.getPassword()));
    }

    static ApplicationUserDataEntry setActiveStatus(boolean status) {
        return userApp ->
                userApp.setActiveStatus(status);
    }

    static ApplicationUserDataEntry setVerifyEmailStatus(boolean status) {
        return userApp ->
                userApp.setVerifyEmailStatus(status);
    }

    static ApplicationUserDataEntry setVerifyPhoneNumberStatus(boolean status) {
        return userApp ->
                userApp.setVerifyPhoneNumberStatus(status);
    }

    static ApplicationUserDataEntry setDeletedStatus(boolean status) {
        return userApp ->
                userApp.setDeletedStatus(status);
    }

    default ApplicationUserDataEntry and(ApplicationUserDataEntry userAppDataEntry){
        return userApp -> {
            this.accept(userApp);
            userAppDataEntry.accept(userApp);
        };
    }
}
