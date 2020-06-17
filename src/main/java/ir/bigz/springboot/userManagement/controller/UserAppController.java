package ir.bigz.springboot.userManagement.controller;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.service.UserAppService;
import ir.bigz.springboot.userManagement.viewmodel.ChangePasswordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/userApp")
public class UserAppController {

    private final UserAppService userAppService;

    @Autowired
    public UserAppController(@Qualifier("UserAppServiceImpl") UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @GetMapping(path = "/allUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<UserApp> getAllUser() {
        return userAppService.getAllUser();
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody UserApp userModel) {
        userAppService.saveUser(userModel);
        return "user add successfully and please validate email";
    }

    @GetMapping(path = "/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserApp getUserById(@PathVariable("userAppId") long id){
        Optional<UserApp> userApp = userAppService.getUserById(id);
        return userApp.get();
    }

    @PutMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String updateUser(@RequestBody UserApp userModel) {
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
}
