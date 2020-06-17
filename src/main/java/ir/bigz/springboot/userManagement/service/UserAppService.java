package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.viewmodel.ChangePasswordModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserAppService {

    List<UserApp> getAllUser();

    void saveUser(UserApp userApp);

    void updateUser(UserApp userApp);

    void deleteUser(long id);

    void activeUser(long id);

    Optional<UserApp> getUserById(long id);

    void emailVerifyingForUser(String userEmail,int userInfoHash);

    void changePassword(String email, ChangePasswordModel changePasswordModel);

    void changePasswordForForgotPassword(String email, ChangePasswordModel changePasswordModel);

}
