package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.dto.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserAppServiceImpl implements UserAppService{

    private final UserAppRepository userAppRepository;

    @Autowired
    public UserAppServiceImpl(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    @Override
    public List<UserApp> getAllUser() {
        return userAppRepository.findAll();
    }

    @Override
    public void saveUser(UserApp userApp) {
        userAppRepository.save(userApp);
    }

    @Override
    public void updateUser(UserApp userApp) {
        Optional<UserApp> userAppOptionalFindById = getUserById(userApp.getId());
        if(userAppOptionalFindById.isPresent()){
            UserApp userAppFromDb = userAppOptionalFindById.get();
            userAppFromDb = userApp;
            userAppRepository.save(userAppFromDb);
        }
        else {
            System.out.println("user not exist");
        }
    }

    @Override
    public void deleteUser(long id) {
        userAppRepository.deleteById(id);
    }

    @Override
    public Optional<UserApp> getUserById(long id) {
        return userAppRepository.findById(id);
    }
}
