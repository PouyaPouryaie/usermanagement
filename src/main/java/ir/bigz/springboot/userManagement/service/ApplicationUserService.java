package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import ir.bigz.springboot.userManagement.viewmodel.ChangePasswordModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ApplicationUserService {

    List<ApplicationUser> getAllUser();

    void addUser(ApplicationUser applicationUser);

    void updateUser(ApplicationUser applicationUser, Authentication authentication);

    void deleteUser(long id);

    void activeUser(long id);

    ApplicationUser getApplicationUserByIdBasedAuthorize(long id, Authentication authentication);

    Optional<ApplicationUser> getApplicationUserById(long id);

    boolean findUser(String userName);

    String createTokenForSignUpUser(String userName);

    void emailVerifyingForUser(String userEmail,int userInfoHash);

    void changePassword(String email, ChangePasswordModel changePasswordModel);

    void changePasswordForForgotPassword(String email, ChangePasswordModel changePasswordModel);

}
