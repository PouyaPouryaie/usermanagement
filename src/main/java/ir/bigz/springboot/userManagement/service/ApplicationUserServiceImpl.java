package ir.bigz.springboot.userManagement.service;

import io.jsonwebtoken.Jwts;
import ir.bigz.springboot.userManagement.auth.UserDetailServiceImpl;
import ir.bigz.springboot.userManagement.auth.UserPrincipal;
import ir.bigz.springboot.userManagement.dao.ApplicationUserDao;
import ir.bigz.springboot.userManagement.dao.ApplicationUserRoleDao;
import ir.bigz.springboot.userManagement.jwt.JwtConfig;
import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import ir.bigz.springboot.userManagement.domain.ApplicationUserRole;
import ir.bigz.springboot.userManagement.utils.*;
import ir.bigz.springboot.userManagement.viewmodel.ChangePasswordModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

import static ir.bigz.springboot.userManagement.utils.ApplicationUserValidation.*;

@Component("ApplicationUserServiceImpl")
@Slf4j
public class ApplicationUserServiceImpl implements ApplicationUserService{

    private final ApplicationUserDao applicationUserDao;
    private final ApplicationUserRoleDao applicationUserRoleDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final UserDetailServiceImpl userDetailService;
    private final SenderMessage senderMessage;

    @Autowired
    public ApplicationUserServiceImpl(ApplicationUserDao applicationUserDao,
                                      ApplicationUserRoleDao applicationUserRoleDao,
                                      PasswordEncoder passwordEncoder,
                                      JwtConfig jwtConfig,
                                      SecretKey secretKey,
                                      UserDetailServiceImpl userDetailService,
                                      @Qualifier("SenderEmailMessage") SenderMessage senderMessage) {
        this.applicationUserDao = applicationUserDao;
        this.applicationUserRoleDao = applicationUserRoleDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.userDetailService = userDetailService;
        this.senderMessage = senderMessage;
    }

    @Override
    public List<ApplicationUser> getAllUser() {
        return applicationUserDao.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(ApplicationUser applicationUser) {

        if(validationApplicationUserForSaveProcess(applicationUser)== ValidationResult.SUCCESS){

            applicationUserDataEntryForSave(applicationUser);
            applicationUser.hashCode();
            callSenderMessage(applicationUser);
            applicationUserDao.save(applicationUser);
            setRoleForApplicationUser(applicationUser);
            applicationUserDao.save(applicationUser);
            if(findUser(applicationUser.getUserName())){
                log.info(String.format("%s added successfully", applicationUser.getUserName()));
            }
            else{
                log.error(String.format("%s added failed", applicationUser.getUserName()));
            }
        }
    }

    @Override
    public void updateUser(ApplicationUser applicationUser) {
        try {
            ApplicationUser basicApplicationUser = applicationUserExistById(applicationUser.getId());
            validationUserAppForUpdate(applicationUser);
            applicationUserDataEntryForUpdate(basicApplicationUser, applicationUser);
            applicationUserDao.save(basicApplicationUser);
        }catch (IllegalStateException e){
            log.error("update process has problem \n" + e.getMessage());
        }
    }

    @Override
    public void deleteUser(long id) {
        Optional<ApplicationUser> userAppOptionalFindById = getApplicationUserById(id);
        if(userAppOptionalFindById.isPresent()){
            deleteUserAppProcess(userAppOptionalFindById.get());
        }else{
            System.out.println(String.format("user by %s id not found", id));
        }
    }

    @Override
    public void activeUser(long id) {
        Optional<ApplicationUser> userAppOptionalFindById = getApplicationUserById(id);
        if(userAppOptionalFindById.isPresent()){
            activeUserAppProcess(userAppOptionalFindById.get());
        }else{
            System.out.println(String.format("user by %s id not found", id));
        }
    }

    @Override
    public boolean findUser(String userName){
        Optional<ApplicationUser> applicationUser = applicationUserDao.findApplicationUserByUserName(userName);
        return applicationUser.isPresent();
    }

    @Override
    public Optional<ApplicationUser> getApplicationUserById(long id) {
        return applicationUserDao.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
    public String createTokenForSignUpUser(String userName) {

        UserPrincipal userPrincipal = (UserPrincipal) userDetailService.loadUserByUsername(userName);
        if(userPrincipal.getUsername()!=null){
            String token = Jwts.builder()
                    .setSubject(userPrincipal.getUsername())
                    .claim("authorities", userPrincipal.getAuthorities())
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                    .signWith(secretKey)
                    .compact();

            String jwtToken = jwtConfig.getTokenPrefix() + token;
            return jwtToken;
        }else{
            throw new IllegalStateException(String.format("user with %s not found", userName));
        }
    }

    private void callSenderMessage(ApplicationUser userApp){
        senderMessage.sendMessageTo(userApp.getEmail(),
                "email verifier",
                Integer.toString(userApp.hashCode()));
    }

    @Override
    public void emailVerifyingForUser(String userEmail, int userInfoHash) {
        try {
            verifyEmailUserProcess(userEmail, userInfoHash);
        }catch (IllegalStateException e){
            System.out.println("email not verify \n" + e.getMessage());
        }
    }

    @Override
    public void changePassword(String email, ChangePasswordModel changePasswordModel) {
        try {
            ApplicationUser userApp = userAppExistByEmail(email);
            validationChangePasswordProcess(userApp.getPassword(), changePasswordModel);
            userApp.setPassword(EncryptTools.toSHAHash(changePasswordModel.getNewPassword()));
            userApp.setLastUpdateDate(DateAndTimeTools.getTimestampNow());
            applicationUserDao.save(userApp);
        }catch (IllegalStateException e){
            System.out.println("changePassword process has problem \n" + e.getMessage());
        }
    }

    @Override
    public void changePasswordForForgotPassword(String email, ChangePasswordModel changePasswordModel) {
        try {
            ApplicationUser userApp = userAppExistByEmail(email);
            validationChangePasswordForForgotProcess(changePasswordModel);
            userApp.setPassword(EncryptTools.toSHAHash(changePasswordModel.getNewPassword()));
            userApp.setLastUpdateDate(DateAndTimeTools.getTimestampNow());
            applicationUserDao.save(userApp);
        }catch (IllegalStateException e){
            System.out.println("changePasswordForForgotPassword process has problem \n" + e.getMessage());
        }
    }

    private ApplicationUser applicationUserExistById(long id){
        Optional<ApplicationUser> applicationUserOptional = getApplicationUserById(id);

        if(applicationUserOptional.isPresent()){
            return applicationUserOptional.get();
        }else{
            throw new IllegalStateException(String.format("user by %s id not found", id));
        }
    }

    private void applicationUserDataEntryForUpdate(ApplicationUser basicUserApp, ApplicationUser userApp){
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

    private void applicationUserDataEntryForSave(ApplicationUser userApp){
        ApplicationUserDataEntry.setJoinDate()
                .and(ApplicationUserDataEntry.setLastUpdateDate())
                .and(ApplicationUserDataEntry.setPassword(passwordEncoder))
                .and(ApplicationUserDataEntry.setAccountNonExpired(true))
                .and(ApplicationUserDataEntry.setAccountNonLocked(true))
                .and(ApplicationUserDataEntry.setCredentialsNonExpired(true))
                .and(ApplicationUserDataEntry.setEnabled(true))
                .and(ApplicationUserDataEntry.setActiveStatus(true))
                .and(ApplicationUserDataEntry.setDeletedStatus(false))
                .and(ApplicationUserDataEntry.setVerifyEmailStatus(false))
                .and(ApplicationUserDataEntry.setVerifyPhoneNumberStatus(false))
                .accept(userApp);
    }

    private void setRoleForApplicationUser(ApplicationUser applicationUser){
        Optional<ApplicationUserRole> roleEditor = applicationUserRoleDao
                .findApplicationUserRoleByRoleName("ROLE_Editor");
        Set<ApplicationUserRole> applicationUserRoles = new HashSet<>();

        if(roleEditor.isPresent()){
            applicationUserRoles.add(roleEditor.get());
            applicationUser.setApplicationUserRoles(applicationUserRoles);
        }
    }

    private void verifyEmailUserProcess(String userEmail,int userInfoHash){

        Optional<ApplicationUser> userFromDb = applicationUserDao.findUserAppByEmail(userEmail);
        if(userFromDb.isPresent()){
            ApplicationUser userApp = userFromDb.get();
            if(VerifyDomainTools.isEmailVerify(userInfoHash).test(userApp)){
                userApp.setVerifyEmailStatus(true);
                applicationUserDao.save(userApp);
            }
            else{
                throw new IllegalStateException("not email verify");
            }
        }
    }

    private void validationUserAppForUpdate(ApplicationUser userApp){
        if(validationUserAppForUpdateProcess(userApp) != ValidationResult.SUCCESS){
            throw new IllegalStateException("UserApp data validation for update not pass");
        }
    }

    private ValidationResult validationUserAppForUpdateProcess(ApplicationUser userApp){
        return ApplicationUserValidation
                .isUserNameNotNull()
                .and(isEmailNotNull())
                .and(isPhoneNumberNotNull())
                .and(isEmailValid())
                .and(isPhoneNumberValid())
                .apply(userApp);
    }

    private ValidationResult validationApplicationUserForSaveProcess(ApplicationUser userApp){
        return ApplicationUserValidation
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

    private ApplicationUser userAppExistByEmail(String email){
        Optional<ApplicationUser> userAppOptional = applicationUserDao.findUserAppByEmail(email);
        if(userAppOptional.isPresent()){
            return userAppOptional.get();
        }else{
            throw new IllegalStateException(String.format("user by  email: %s not found", email));
        }
    }

    private void deleteUserAppProcess(ApplicationUser userApp){
        userApp.setDeletedStatus(true);
        userApp.setActiveStatus(false);
        applicationUserDao.save(userApp);
    }

    private void activeUserAppProcess(ApplicationUser userApp){
        userApp.setDeletedStatus(false);
        userApp.setActiveStatus(true);
        applicationUserDao.save(userApp);
    }
}
