package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.domain.UserApp;
import ir.bigz.springboot.userManagement.dto.UserAppRepository;
import ir.bigz.springboot.userManagement.utils.*;
import ir.bigz.springboot.userManagement.viewmodel.ChangePasswordModel;
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

        if(validationUserAppForSaveProcess(userApp)==ValidationResult.SUCCESS){
            userAppDataEntryForSave(userApp);
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
        try {
            UserApp basicUserApp = userAppExistById(userApp.getId());
            validationUserAppForUpdate(userApp);
            userAppDataEntryForUpdate(basicUserApp, userApp);
            userAppRepository.save(basicUserApp);
        }catch (IllegalStateException e){
            System.out.println("update process has problem \n" + e.getMessage());
        }
    }

    @Override
    public void deleteUser(long id) {
        Optional<UserApp> userAppOptionalFindById = getUserById(id);
        if(userAppOptionalFindById.isPresent()){
            deleteUserAppProcess(userAppOptionalFindById.get());
        }else{
            System.out.println(String.format("user by %s id not found", id));
        }
    }

    @Override
    public void activeUser(long id) {
        Optional<UserApp> userAppOptionalFindById = getUserById(id);
        if(userAppOptionalFindById.isPresent()){
            activeUserAppProcess(userAppOptionalFindById.get());
        }else{
            System.out.println(String.format("user by %s id not found", id));
        }
    }

    @Override
    public Optional<UserApp> getUserById(long id) {
        return userAppRepository.findById(id);
    }

    @Override
    public void emailVerifyingForUser(String userEmail,int userInfoHash) {

        try {
            verifyEmailUserProcess(userEmail, userInfoHash);
        }catch (IllegalStateException e){
            System.out.println("email not verify \n" + e.getMessage());
        }
    }

    @Override
    public void changePassword(String email, ChangePasswordModel changePasswordModel) {

        try {
            UserApp userApp = userAppExistByEmail(email);
            validationChangePasswordProcess(userApp.getPassword(), changePasswordModel);
            userApp.setPassword(EncryptTools.toSHAHash(changePasswordModel.getNewPassword()));
            userApp.setLastUpdateDate(DateAndTimeTools.getTimestampNow());
            userAppRepository.save(userApp);
        }catch (IllegalStateException e){
            System.out.println("changePassword process has problem \n" + e.getMessage());
        }
    }

    @Override
    public void changePasswordForForgotPassword(String email, ChangePasswordModel changePasswordModel) {
        try {
            UserApp userApp = userAppExistByEmail(email);
            validationChangePasswordForForgotProcess(changePasswordModel);
            userApp.setPassword(EncryptTools.toSHAHash(changePasswordModel.getNewPassword()));
            userApp.setLastUpdateDate(DateAndTimeTools.getTimestampNow());
            userAppRepository.save(userApp);
        }catch (IllegalStateException e){
            System.out.println("changePasswordForForgotPassword process has problem \n" + e.getMessage());
        }
    }

    private UserApp userAppExistById(long id){
        Optional<UserApp> userAppOptional = getUserById(id);

        if(userAppOptional.isPresent()){
            return userAppOptional.get();
        }else{
            throw new IllegalStateException(String.format("user by %s id not found", id));
        }
    }

    private UserApp userAppExistByEmail(String email){
        Optional<UserApp> userAppOptional = userAppRepository.findUserAppByEmail(email);
        if(userAppOptional.isPresent()){
            return userAppOptional.get();
        }else{
            throw new IllegalStateException(String.format("user by  email: %s not found", email));
        }
    }

    private UserApp userAppDataEntryForSave(UserApp userApp){
        UserAppDataEntry.setJoinDate()
                .and(UserAppDataEntry.setLastUpdateDate())
                .and(UserAppDataEntry.setPassword())
                .and(UserAppDataEntry.setActiveStatus(true))
                .and(UserAppDataEntry.setDeletedStatus(false))
                .and(UserAppDataEntry.setVerifyEmailStatus(false))
                .and(UserAppDataEntry.setVerifyPhoneNumberStatus(false))
                .accept(userApp);
        return userApp;
    }

    private void userAppDataEntryForUpdate(UserApp basicUserApp, UserApp userApp){
        basicUserApp.setUserName(userApp.getUserName());
        basicUserApp.setLastUpdateDate(DateAndTimeTools.getTimestampNow());
        basicUserApp.setFirstName(userApp.getFirstName());
        basicUserApp.setLastName(userApp.getLastName());
        if(basicUserApp.getEmail() != userApp.getEmail()){
            basicUserApp.setEmail(userApp.getEmail());
            basicUserApp.setVerifyEmailStatus(false);
            callSenderMessage(basicUserApp);
        }
        if(basicUserApp.getPhoneNumber() != userApp.getPhoneNumber()){
            basicUserApp.setPhoneNumber(userApp.getPhoneNumber());
            basicUserApp.setVerifyPhoneNumberStatus(false);
        }
        basicUserApp.setHashCode(0);
        basicUserApp.getHashCode();
    }

    private void callSenderMessage(UserApp userApp){
        senderMessage.sendMessageTo(userApp.getEmail(),
                "email verifier",
                Integer.toString(userApp.hashCode()));
    }

    private ValidationResult validationUserAppForSaveProcess(UserApp userApp){
        return UserAppValidation
                .isUserNameNotNull()
                .and(isEmailNotNull())
                .and(isPhoneNumberNotNull())
                .and(isEmailValid())
                .and(isPhoneNumberValid())
                .and(isQuestionAndAnswerNotEmpty())
                .apply(userApp);
    }

    private void validationChangePasswordProcess(String currentPasswordFromDB, ChangePasswordModel changePasswordModel){

        if(PasswordValidationTools.isTwoPasswordSame(EncryptTools.toSHAHash(
                changePasswordModel.getNewPassword()))
                .test(currentPasswordFromDB)){
            throw new IllegalStateException("current password from model not same with current password in db");
        }
        if(!PasswordValidationTools.isTwoPasswordSame(
                changePasswordModel.getRetryPassword())
                .test(changePasswordModel.getNewPassword())){
            throw new IllegalStateException("retry password not same with new password");
        }
        if(PasswordValidationTools.isTwoPasswordSame(
                changePasswordModel.getNewPassword())
                .test(changePasswordModel.getCurrentPassword())){
            throw new IllegalStateException("new password same with old password");
        }
    }

    private void validationChangePasswordForForgotProcess(ChangePasswordModel changePasswordModel){

        if(!PasswordValidationTools.isTwoPasswordSame(
                changePasswordModel.getRetryPassword())
                .test(changePasswordModel.getNewPassword())){
            throw new IllegalStateException("retry password not same with new password");
        }
    }

    private void validationUserAppForUpdate(UserApp userApp){
        if(validationUserAppForUpdateProcess(userApp) != ValidationResult.SUCCESS){
            throw new IllegalStateException("UserApp data validation for update not pass");
        }
    }

    private ValidationResult validationUserAppForUpdateProcess(UserApp userApp){
        return UserAppValidation
                .isUserNameNotNull()
                .and(isEmailNotNull())
                .and(isPhoneNumberNotNull())
                .and(isEmailValid())
                .and(isPhoneNumberValid())
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

    private void deleteUserAppProcess(UserApp userApp){
        userApp.setDeletedStatus(true);
        userApp.setActiveStatus(false);
        userAppRepository.save(userApp);
    }

    private void activeUserAppProcess(UserApp userApp){
        userApp.setDeletedStatus(false);
        userApp.setActiveStatus(true);
        userAppRepository.save(userApp);
    }
}
