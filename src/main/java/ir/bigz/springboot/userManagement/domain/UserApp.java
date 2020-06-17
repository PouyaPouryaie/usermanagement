package ir.bigz.springboot.userManagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "user_app")
@Access(AccessType.FIELD)
public class UserApp {

    @Id
    @GeneratedValue
    @Column(name = "user_app_id")
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @ElementCollection
    @MapKeyColumn(name="question")
    @Column(name="answer")
    @CollectionTable(name="question_and_answer", joinColumns=@JoinColumn(name="user_app_id"))
    private Map<String, String> questionAndAnswerMap = new HashMap<>();
    @Column(name="count_failed_in_latest_login")
    private int countFailedInLatestLogin;
    @Column(name= "join_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Timestamp joinDate;
    @Column(name= "last_update_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Timestamp lastUpdateDate;
    @Column(name = "active_status")
    private boolean activeStatus;
    @Column(name = "deleted_status")
    private boolean deletedStatus;
    @Column(name = "verify_phone_number_status")
    private boolean verifyPhoneNumberStatus;
    @Column(name = "verify_email_status")
    private boolean verifyEmailStatus;
    @Column(name = "hash_code")
    private int hashCode;

    public UserApp() {
    }

    @Override
    public UserApp clone() throws IllegalStateException {
        try {
            UserApp userApp = new UserApp();
            userApp.setId(this.getId());
            userApp.setFirstName(this.getFirstName());
            userApp.setUserName(this.getUserName());
            userApp.setLastName(this.getLastName());
            userApp.setEmail(this.getEmail());
            userApp.setPhoneNumber(this.getPhoneNumber());
            userApp.setPassword(this.getPassword());
            userApp.setVerifyPhoneNumberStatus(this.isVerifyPhoneNumberStatus());
            userApp.setVerifyEmailStatus(this.isVerifyEmailStatus());
            userApp.setDeletedStatus(this.isDeletedStatus());
            userApp.setActiveStatus(this.isActiveStatus());
            userApp.setQuestionAndAnswerMap(this.getQuestionAndAnswerMap());
            userApp.setJoinDate(this.getJoinDate());
            userApp.setLastUpdateDate(this.getLastUpdateDate());
            return userApp;
        }catch (IllegalStateException e){
            throw new AssertionError("userApp clone has exception \n" + e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserApp userApp = (UserApp) o;
        return Objects.equals(firstName, userApp.firstName) &&
                Objects.equals(lastName, userApp.lastName) &&
                Objects.equals(userName, userApp.userName) &&
                Objects.equals(email, userApp.email) &&
                Objects.equals(phoneNumber, userApp.phoneNumber) &&
                Objects.equals(password, userApp.password) &&
                Objects.equals(joinDate, userApp.joinDate);
    }

    @Override
    public int hashCode() {
        int result = this.getHashCode();
        if(result == 0) {
            result = Objects.hash(firstName, lastName, userName, email, phoneNumber, password, joinDate);
            this.setHashCode(result);
        }

        return result;
    }

    @Override
    public String toString() {
        return "UserApp{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", questionAndAnswerMap=" + questionAndAnswerMap +
                ", countFailedInLatestLogin=" + countFailedInLatestLogin +
                ", joinDate=" + joinDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", activeStatus=" + activeStatus +
                ", deletedStatus=" + deletedStatus +
                ", verifyPhoneNumberStatus=" + verifyPhoneNumberStatus +
                ", verifyEmailStatus=" + verifyEmailStatus +
                ", hashCode=" + hashCode +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCountFailedInLatestLogin() {
        return countFailedInLatestLogin;
    }

    public void setCountFailedInLatestLogin(int countFailedInLatestLogin) {
        this.countFailedInLatestLogin = countFailedInLatestLogin;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public boolean isDeletedStatus() {
        return deletedStatus;
    }

    public void setDeletedStatus(boolean deletedStatus) {
        this.deletedStatus = deletedStatus;
    }

    public boolean isVerifyPhoneNumberStatus() {
        return verifyPhoneNumberStatus;
    }

    public void setVerifyPhoneNumberStatus(boolean verifyPhoneNumberStatus) {
        this.verifyPhoneNumberStatus = verifyPhoneNumberStatus;
    }

    public boolean isVerifyEmailStatus() {
        return verifyEmailStatus;
    }

    public void setVerifyEmailStatus(boolean verifyEmailStatus) {
        this.verifyEmailStatus = verifyEmailStatus;
    }

    public Map<String, String> getQuestionAndAnswerMap() {
        return questionAndAnswerMap;
    }

    public void setQuestionAndAnswerMap(Map<String, String> questionAndAnswerMap) {
        this.questionAndAnswerMap = questionAndAnswerMap;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }
}
