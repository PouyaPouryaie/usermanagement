package ir.bigz.springboot.userManagement.controller;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.service.UserAppService;
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
    @ResponseStatus(HttpStatus.CREATED)
    public String updateUser(@RequestBody UserApp userModel) {
        userAppService.updateUser(userModel);
        return "user update successfully";
    }

    @DeleteMapping(path = "/delete/{userAppId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String deleteUserById(@PathVariable("userAppId") long id) {
        userAppService.deleteUser(id);
        return "user delete successfully";
    }

    @PostMapping(path = "/emailVerifier/{userEmail}/{userInfoHash}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String emailVerify(@PathVariable("userEmail") String userEmail,@PathVariable("userInfoHash") int userInfoHash) {
        userAppService.emailVerifyingForUser(userEmail,userInfoHash);
        return "email verify";
    }
}
