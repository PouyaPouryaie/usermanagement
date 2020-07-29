package ir.bigz.springboot.userManagement.usermanagement;

import ir.bigz.springboot.userManagement.utils.PasswordValidationTools;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PasswordValidationTest {

    @ParameterizedTest
    @CsvSource({"12345, 12345, true",
            "12345, 1234, false"
    })
    void itShouldSamePassword(String firstPassword, String secondPassword, boolean expected) {

        //when
        boolean isValid = PasswordValidationTools.isTwoPasswordSame(secondPassword).test(firstPassword);

        //Then
        assertThat(isValid).isEqualTo(expected);
    }
}
