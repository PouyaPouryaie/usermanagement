package ir.bigz.springboot.userManagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * {
 * 	"firstName":"pouya",
 * 	"lastName":"pouryaie",
 * 	"userName":"mr.po",
 * 	"email":"pouyapouryaie@gmail.com",
 * 	"phoneNumber":"9388773155",
 * 	"password":"123456",
 * 	"activeStatus":true,
 * 	"deletedStatus":false,
 * 	"verifyPhoneNumberStatus":false,
 * 	"verifyEmailStatus":false
 * }
 */


@Entity
@Table(name = "userApp")
@Access(AccessType.FIELD)
public class UserApp {

    @Id
    @GeneratedValue
    @Column(name = "userAppId")
    private long id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "userName")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "password")
    private String password;

    @ElementCollection
    @MapKeyColumn(name="Question")
    @Column(name="Answer")
    @CollectionTable(name="QuestionAndAnswer", joinColumns=@JoinColumn(name="userAppId"))
    private Map<String, String> questionAndAnswerMap = new HashMap<>();

    @Column(name="countFailedInLatestLogin")
    private int countFailedInLatestLogin;

    @Column(name= "joinDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Timestamp joinDate;

    @Column(name = "activeStatus")
    private boolean activeStatus;

    @Column(name = "deletedStatus")
    private boolean deletedStatus;

    @Column(name = "verifyPhoneNumberStatus")
    private boolean verifyPhoneNumberStatus;

    @Column(name = "verifyEmailStatus")
    private boolean verifyEmailStatus;


    public UserApp() {
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
                ", activeStatus=" + activeStatus +
                ", deletedStatus=" + deletedStatus +
                ", verifyPhoneNumberStatus=" + verifyPhoneNumberStatus +
                ", verifyEmailStatus=" + verifyEmailStatus +
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
}
