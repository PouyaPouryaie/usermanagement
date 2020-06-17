package ir.bigz.springboot.userManagement.viewmodel;

public class ChangePasswordModel {

    private final String email;
    private final String currentPassword;
    private final String newPassword;
    private final String retryPassword;

    public ChangePasswordModel(String email, String currentPassword, String newPassword, String retryPassword) {
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.retryPassword = retryPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRetryPassword() {
        return retryPassword;
    }
}
