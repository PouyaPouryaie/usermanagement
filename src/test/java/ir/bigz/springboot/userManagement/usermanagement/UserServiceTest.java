package ir.bigz.springboot.userManagement.usermanagement;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import ir.bigz.springboot.userManagement.service.ApplicationUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Qualifier("ApplicationUserServiceImpl")
    @Autowired
    private ApplicationUserService userAppService;

    private ApplicationUser userApp;

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
        userApp.setQuestionAndAnswerMap(QAndA);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED)
    void itShouldSaveUserApp() {

        //Get
        ApplicationUser userAppSample = userApp;

        //when
        userAppService.addUser(userAppSample);

        //then
        Optional<ApplicationUser> optionalUser = userAppService.getApplicationUserById(userAppSample.getId());
        assertThat(optionalUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c).isEqualToComparingFieldByField(userApp);
        });
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    void itShouldNotEqual(){

        //get
        ApplicationUser userAppSample = userAppService.getApplicationUserById(1).get();
        ApplicationUser backup = userAppSample.clone();

        //when
        userAppSample.setEmail("pouyapouryaie@yahoo.com");

        //then
        assertThat(userAppSample).isNotEqualTo(backup);
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED)
    void itShouldNotUpdateUserApp() throws Exception{

        //Get
        userAppService.addUser(userApp);
        ApplicationUser userAppSample = userAppService.getApplicationUserById(1).get();
        ApplicationUser backup = userAppSample.clone();

        backup.setEmail("pouyapouryaie@yahoo.com");
        backup.setUserName(null);

        //when
        userAppService.updateUser(backup);

        //then
        Optional<ApplicationUser> optionalUser = userAppService.getApplicationUserById(userAppSample.getId());
        assertThat(optionalUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getLastUpdateDate()).isEqualTo(backup.getLastUpdateDate());
        });
    }

    @Test
    @Transactional(propagation= Propagation.REQUIRED)
    void itShouldUpdateUserApp() throws Exception{

        //Get
        ApplicationUser userAppSample = userApp;
        userAppService.addUser(userAppSample);
        ApplicationUser backup = userAppSample.clone();
        userAppSample.setEmail("pouyapouryaie@yahoo.com");

        //when
        userAppService.updateUser(userAppSample);

        //then
        Optional<ApplicationUser> optionalUser = userAppService.getApplicationUserById(userAppSample.getId());
        assertThat(optionalUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getLastUpdateDate()).isEqualTo(backup.getLastUpdateDate());
        });
    }
}
