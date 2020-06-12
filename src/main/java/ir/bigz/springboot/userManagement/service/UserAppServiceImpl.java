package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.dto.UserAppRepository;
import ir.bigz.springboot.userManagement.utils.DateAndTimeTools;
import ir.bigz.springboot.userManagement.utils.EncryptTools;
import ir.bigz.springboot.userManagement.utils.UserAppValidation;
import ir.bigz.springboot.userManagement.utils.VerifyDomainTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import static ir.bigz.springboot.userManagement.utils.UserAppValidation.*;

@Component("UserAppServiceImpl")
public class UserAppServiceImpl implements UserAppService{

    private final UserAppRepository userAppRepository;
    private final SenderMessage senderMessage;

    @Autowired
    public UserAppServiceImpl(UserAppRepository userAppRepository,
                              @Qualifier("SenderEmailMessage") SenderMessage senderMessage) {
        this.userAppRepository = userAppRepository;
        this.senderMessage = senderMessage;
    }

    @Override
    public List<UserApp> getAllUser() {
        return userAppRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(UserApp userApp) {

        if(validationUserAppProcess(userApp)==ValidationResult.SUCCESS){
            userApp.setJoinDate(DateAndTimeTools.getTimestampNow());
            userApp.setPassword(EncryptTools.toSHAHash(userApp.getPassword()));
            userApp.setActiveStatus(true);
            userApp.setDeletedStatus(false);
            userApp.setVerifyEmailStatus(false);
            userApp.setVerifyPhoneNumberStatus(false);
            userApp.hashCode();
            callSenderMessage(userApp);
            userAppRepository.save(userApp);
        }
        else{
            throw new IllegalStateException("validation user have error");
        }
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

    @Override
    public void emailVerifyingForUser(String userEmail,int userInfoHash) {

        try {
            verifyEmailUserProcess(userEmail, userInfoHash);
        }catch (IllegalStateException err){
            System.out.println("email not verify \n" + err.getMessage());
        }
    }

    private void callSenderMessage(UserApp userApp){
        senderMessage.sendMessageTo(userApp.getEmail(),
                "email verifier",
                Integer.toString(userApp.hashCode()));
    }

    private ValidationResult validationUserAppProcess(UserApp userApp){
        return UserAppValidation
                .isEmailValid()
                .and(isPhoneNumberValid())
                .and(isQuestionAndAnswerNotEmpty())
                .apply(userApp);
    }

    private void verifyEmailUserProcess(String userEmail,int userInfoHash){

        Optional<UserApp> userFromDb = userAppRepository.findUserAppByEmail(userEmail);
        if(userFromDb.isPresent()){
            UserApp userApp = userFromDb.get();
            if(VerifyDomainTools.isEmailVerify(userInfoHash).test(userApp)){
                userApp.setVerifyEmailStatus(true);
                userAppRepository.save(userApp);
            }
            else{
                throw new IllegalStateException("not email verify");
            }
        }
    }
}
