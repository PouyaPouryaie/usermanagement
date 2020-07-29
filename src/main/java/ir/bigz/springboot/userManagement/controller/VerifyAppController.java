package ir.bigz.springboot.userManagement.controller;

import ir.bigz.springboot.userManagement.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/verify")
public class VerifyAppController {

    private final ApplicationUserService userAppService;

    @Autowired
    public VerifyAppController(ApplicationUserService applicationUserService) {
        this.userAppService = applicationUserService;
    }

    @GetMapping(path = "/emailVerifier/{userEmail}/{userInfoHash}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String emailVerify(@PathVariable("userEmail") String userEmail, @PathVariable("userInfoHash") int userInfoHash) {
        userAppService.emailVerifyingForUser(userEmail,userInfoHash);
        return "email verify";
    }
}
