package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.domain.UserApp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserAppService {

    List<UserApp> getAllUser();

    void saveUser(UserApp userApp);

    void updateUser(UserApp userApp);

    void deleteUser(long id);

    Optional<UserApp> getUserById(long id);

}
