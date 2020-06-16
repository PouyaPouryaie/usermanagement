package ir.bigz.springboot.userManagement.utils;

import ir.bigz.springboot.userManagement.domain.UserApp;

import java.util.function.Consumer;

public interface UserAppDataEntry extends Consumer<UserApp> {

    static UserAppDataEntry setJoinDate() {
        return userApp ->
                userApp.setJoinDate(DateAndTimeTools.getTimestampNow());
    }

    static UserAppDataEntry setLastUpdateDate() {
        return userApp ->
                userApp.setLastUpdateDate(DateAndTimeTools.getTimestampNow());
    }

    static UserAppDataEntry setPassword() {
        return userApp ->
                userApp.setPassword(EncryptTools.toSHAHash(userApp.getPassword()));
    }

    static UserAppDataEntry setActiveStatus(boolean status) {
        return userApp ->
                userApp.setActiveStatus(status);
    }

    static UserAppDataEntry setVerifyEmailStatus(boolean status) {
        return userApp ->
                userApp.setVerifyEmailStatus(status);
    }

    static UserAppDataEntry setVerifyPhoneNumberStatus(boolean status) {
        return userApp ->
                userApp.setVerifyPhoneNumberStatus(status);
    }

    static UserAppDataEntry setDeletedStatus(boolean status) {
        return userApp ->
                userApp.setDeletedStatus(status);
    }

    default UserAppDataEntry and(UserAppDataEntry userAppDataEntry){
        return userApp -> {
            this.accept(userApp);
            userAppDataEntry.accept(userApp);
        };
    }
}
