package ir.bigz.springboot.userManagement.usermanagement;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import ir.bigz.springboot.userManagement.utils.ApplicationUserDataEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableTransactionManagement
public class UserDataEntryTest {

    ApplicationUser userApp;

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
    void itShouldDataEntry(){

        //When
        ApplicationUserDataEntry.setJoinDate().and(ApplicationUserDataEntry.setLastUpdateDate()).accept(userApp);

        //Then
        assertThat(userApp.getLastUpdateDate()).isNotNull();
    }
}
