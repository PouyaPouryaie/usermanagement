package ir.bigz.springboot.userManagement.usermanagement;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.service.UserAppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableTransactionManagement
public class UserServiceTest {

    @Autowired
    private UserAppService userAppService;

    private UserApp userApp;

    @BeforeEach
    void setUp(){

        Map<String, String> QAndA = new HashMap<>();
        QAndA.put("how old are you", "27");

        userApp = new UserApp();
        userApp.setFirstName("pouya");
        userApp.setUserName("mr.po");
        userApp.setLastName("pouryaie");
        userApp.setEmail("pouyapouryaie@hotmail.com");
        userApp.setPhoneNumber("09388773155");
        userApp.setPassword("123456");
        userApp.setQuestionAndAnswerMap(QAndA);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED)
    void itShouldSaveUserApp() {

        //Get
        UserApp userAppSample = userApp;

        //when
        userAppService.saveUser(userAppSample);

        //then
        Optional<UserApp> optionalUser = userAppService.getUserById(userAppSample.getId());
        assertThat(optionalUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c).isEqualToComparingFieldByField(userApp);
        });
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void itShouldNotEqual(){

        //get
        UserApp userAppSample = userAppService.getUserById(1).get();
        UserApp backup = userAppSample.clone();

        //when
        userAppSample.setEmail("pouyapouryaie@yahoo.com");

        //then
        assertThat(userAppSample).isNotEqualTo(backup);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED)
    void itShouldNotUpdateUserApp() throws Exception{

        //Get
        userAppService.saveUser(userApp);
        UserApp userAppSample = userAppService.getUserById(1).get();
        UserApp backup = userAppSample.clone();

        backup.setEmail("pouyapouryaie@yahoo.com");
        backup.setUserName(null);

        //when
        userAppService.updateUser(backup);

        //then
        Optional<UserApp> optionalUser = userAppService.getUserById(userAppSample.getId());
        assertThat(optionalUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getLastUpdateDate()).isEqualTo(backup.getLastUpdateDate());
        });
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED)
    void itShouldUpdateUserApp() throws Exception{

        //Get
        UserApp userAppSample = userApp;
        userAppService.saveUser(userAppSample);
        UserApp backup = userAppSample.clone();
        userAppSample.setEmail("pouyapouryaie@yahoo.com");

        //when
        userAppService.updateUser(userAppSample);

        //then
        Optional<UserApp> optionalUser = userAppService.getUserById(userAppSample.getId());
        assertThat(optionalUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getLastUpdateDate()).isEqualTo(backup.getLastUpdateDate());
        });
    }
}
