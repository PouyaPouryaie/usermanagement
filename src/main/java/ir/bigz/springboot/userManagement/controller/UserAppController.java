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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ApplicationUser> getAllUser() {
        return userAppService.getAllUser();
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody ApplicationUser userModel) {
        userAppService.addUser(userModel);
        return "user add successfully and please validate email";
    }

    @GetMapping(path = "/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApplicationUser getUserById(@PathVariable("userAppId") long id){
        Optional<ApplicationUser> userApp = userAppService.getApplicationUserById(id);
        return userApp.get();
    }

    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUser(@RequestBody ApplicationUser userModel) {
        userAppService.updateUser(userModel);
        return "user update successfully";
    }

    @DeleteMapping(path = "/delete/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteUserById(@PathVariable("userAppId") long id) {
        userAppService.deleteUser(id);
        return "user delete successfully";
    }

    @PutMapping(path = "/active/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
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
