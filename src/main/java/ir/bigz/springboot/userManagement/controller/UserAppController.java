package ir.bigz.springboot.userManagement.controller;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import ir.bigz.springboot.userManagement.exception.ApiRequestException;
import ir.bigz.springboot.userManagement.service.ApplicationUserService;
import ir.bigz.springboot.userManagement.viewmodel.ChangePasswordModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/userApp")
@Slf4j
public class UserAppController {

    private final ApplicationUserService userAppService;

    @Autowired
    public UserAppController(@Qualifier("ApplicationUserServiceImpl") ApplicationUserService applicationUserService) {
        this.userAppService = applicationUserService;
    }

    @GetMapping(path = "/allUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_Admin')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ApplicationUser> getAllUser() {
        return userAppService.getAllUser();
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_Admin', 'ROLE_AdminTrainer')")
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody ApplicationUser userModel) {
        userAppService.addUser(userModel);
        return "user add successfully and please validate email";
    }

    @GetMapping(path = "/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_Admin', 'ROLE_Editor')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> getUserById(@PathVariable("userAppId") long id){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            ApplicationUser applicationUser = userAppService
                    .getApplicationUserByIdBasedAuthorize(id, authentication);
            return new ResponseEntity<>(applicationUser, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAnyRole('ROLE_Admin', 'ROLE_Editor')")
    public ResponseEntity<?> updateUser(@RequestBody ApplicationUser userModel) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            userAppService.updateUser(userModel, authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping(path = "/delete/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ROLE_Admin')")
    public String deleteUserById(@PathVariable("userAppId") long id) {
        userAppService.deleteUser(id);
        return "user delete successfully";
    }

    @PutMapping(path = "/active/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ROLE_Admin')")
    public String activeUserById(@PathVariable("userAppId") long id) {
        userAppService.activeUser(id);
        return "user active successfully";
    }

    @PostMapping(path = "/changepassword/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String changePassword(@PathVariable("userEmail") String email,@RequestBody ChangePasswordModel changePasswordModel) {
        userAppService.changePassword(email, changePasswordModel);
        return "change password successfully";
    }

    @PostMapping(path = "/changepassword/forgotpassword/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String changePasswordForForgotPassword(@PathVariable("userEmail") String email,@RequestBody ChangePasswordModel changePasswordModel) {
        userAppService.changePasswordForForgotPassword(email, changePasswordModel);
        return "change password successfully";
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUpUser(@RequestBody ApplicationUser applicationUser){
        userAppService.addUser(applicationUser);
        log.info("add User done");
        try {
            String tokenForSignUpUser = userAppService.createTokenForSignUpUser(applicationUser.getUserName());
            return new ResponseEntity<>(tokenForSignUpUser, HttpStatus.CREATED);
        }catch (Exception e){
            log.error("token not created \n" + e.getMessage());
            throw new ApiRequestException("token dose not created");
        }
    }
}
