package ir.bigz.springboot.userManagement.usermanagement;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.dto.UserAppRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@EnableTransactionManagement
public class UserCrudTest {

    @Autowired
    private UserAppRepository userAppRepository;

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
        userApp.setVerifyPhoneNumberStatus(false);
        userApp.setVerifyEmailStatus(false);
        userApp.setDeletedStatus(false);
        userApp.setActiveStatus(true);
        userApp.setQuestionAndAnswerMap(QAndA);
        userApp.setJoinDate(new Timestamp(new Date().getTime()));
    }

    @Test
    @Transactional(propagation=Propagation.REQUIRED)
    void itShouldSaveUserApp() {

        //Get
        UserApp userAppSample = userApp;

        //when
        userAppRepository.save(userAppSample);

        //then
        Optional<UserApp> optionalUser = userAppRepository.findUserAppByEmail(userAppSample.getEmail());
        assertThat(optionalUser).isPresent().hasValueSatisfying(c -> {
            assertThat(c).isEqualToComparingFieldByField(userApp);
        });
    }

    @Test
    @Transactional
    void itShouldSelectCustomerByPhoneNumber(){

        //Get
        UserApp userAppSample = userApp;

        //when
        userAppRepository.save(userAppSample);

        //then
        Optional<UserApp> userAppOptional = userAppRepository.findUserAppByPhone(userAppSample.getPhoneNumber());
        assertThat(userAppOptional).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isNotZero();
        });
    }
}
