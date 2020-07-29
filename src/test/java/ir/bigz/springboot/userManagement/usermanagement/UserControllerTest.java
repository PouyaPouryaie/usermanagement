package ir.bigz.springboot.userManagement.usermanagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.bigz.springboot.userManagement.dao.ApplicationUserDao;
import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import ir.bigz.springboot.userManagement.utils.EncryptTools;
import ir.bigz.springboot.userManagement.viewmodel.ChangePasswordModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationUserDao userAppRepository;

    private ApplicationUser userApp;

    private ChangePasswordModel changePasswordModel;

    @BeforeEach
    void setUp(){

        Map<String, String> QAndA = new HashMap<>();
        QAndA.put("how old are you", "27");

        userApp = new ApplicationUser();
        userApp.setFirstName("pouya");
        userApp.setUserName("mr.po");
        userApp.setLastName("pouryaie");
        userApp.setEmail("pouyapouryaie@hotmail.com");
        userApp.setPhoneNumber("09388773155");
        userApp.setPassword("123456");
        userApp.setVerifyPhoneNumberStatus(false);
        userApp.setVerifyEmailStatus(false);
        userApp.setDeletedStatus(false);
        userApp.setActiveStatus(true);
        userApp.setQuestionAndAnswerMap(QAndA);
        userApp.setJoinDate(new Timestamp(new Date().getTime()));

        changePasswordModel = new ChangePasswordModel("pouyapouryaie@hotmail.com", "123456", "201290", "201290");
    }

    @Test
    @Transactional
    void isShouldCreateUserAppSuccessfully() throws Exception{

        ApplicationUser userAppSample = userApp;

        ResultActions userAppResultAction = mockMvc.perform(post("/api/v1/userApp/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(userAppSample))));


        userAppResultAction.andExpect(status().isCreated());
        assertThat(userAppRepository.findUserAppByEmail(userAppSample.getEmail()))
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p.getEmail()).isEqualTo("pouyapouryaie@hotmail.com"));

    }

    @Test
    @Transactional
    void isShouldForgotPassword() throws Exception{

        ApplicationUser userAppSample = userApp;

        ResultActions userAppResultAction = mockMvc.perform(post("/api/v1/userApp/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(userAppSample))));

        ChangePasswordModel changePasswordModelForTest = changePasswordModel;
        ResultActions forgotPasswordResultAction = mockMvc.perform(post("/api/v1/userApp/changepassword/" + changePasswordModelForTest.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(changePasswordModelForTest))));


        forgotPasswordResultAction.andExpect(status().isAccepted());
        assertThat(userAppRepository.findUserAppByEmail(userAppSample.getEmail()))
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p.getPassword()).isEqualTo(EncryptTools.toSHAHash(changePasswordModel.getNewPassword())));

    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
