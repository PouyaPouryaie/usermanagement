package ir.bigz.springboot.userManagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "application_user")
@Access(AccessType.FIELD)
public class ApplicationUser {

    //serialize and deserialize
    static final long serialVersionUID=4L;

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "isAccountNonExpired", nullable = false)
    private boolean isAccountNonExpired;

    @Column(name = "isAccountNonLocked", nullable = false)
    private boolean isAccountNonLocked;

    @Column(name = "isCredentialsNonExpired", nullable = false)
    private boolean isCredentialsNonExpired;

    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;

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


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "application_user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<ApplicationUserRole> applicationUserRoles = new HashSet<>();


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "application_user_permission",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "permission_id") }
    )
    private Set<ApplicationUserPermission> applicationUserPermissions = new HashSet<>();

    public ApplicationUser(){

    }

    @Override
    public ApplicationUser clone() throws IllegalStateException {
        try {
            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setId(this.getId());
            applicationUser.setFirstName(this.getFirstName());
            applicationUser.setUserName(this.getUserName());
            applicationUser.setLastName(this.getLastName());
            applicationUser.setEmail(this.getEmail());
            applicationUser.setPhoneNumber(this.getPhoneNumber());
            applicationUser.setPassword(this.getPassword());
            applicationUser.setVerifyPhoneNumberStatus(this.isVerifyPhoneNumberStatus());
            applicationUser.setVerifyEmailStatus(this.isVerifyEmailStatus());
            applicationUser.setDeletedStatus(this.isDeletedStatus());
            applicationUser.setActiveStatus(this.isActiveStatus());
            applicationUser.setQuestionAndAnswerMap(this.getQuestionAndAnswerMap());
            applicationUser.setJoinDate(this.getJoinDate());
            applicationUser.setLastUpdateDate(this.getLastUpdateDate());
            applicationUser.setAccountNonExpired(this.isAccountNonExpired());
            applicationUser.setAccountNonLocked(this.isAccountNonLocked());
            applicationUser.setCredentialsNonExpired(this.isCredentialsNonExpired());
            applicationUser.setEnabled(this.isEnabled());
            return applicationUser;
        }catch (IllegalStateException e){
            throw new AssertionError("userApp clone has exception \n" + e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUser userApp = (ApplicationUser) o;
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

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Map<String, String> getQuestionAndAnswerMap() {
        return questionAndAnswerMap;
    }

    public void setQuestionAndAnswerMap(Map<String, String> questionAndAnswerMap) {
        this.questionAndAnswerMap = questionAndAnswerMap;
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

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public Set<ApplicationUserRole> getApplicationUserRoles() {
        return applicationUserRoles;
    }

    public void setApplicationUserRoles(Set<ApplicationUserRole> applicationUserRoles) {
        this.applicationUserRoles = applicationUserRoles;
    }

    public Set<ApplicationUserPermission> getApplicationUserPermissions() {
        return applicationUserPermissions;
    }

    public void setApplicationUserPermissions(Set<ApplicationUserPermission> applicationUserPermissions) {
        this.applicationUserPermissions = applicationUserPermissions;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", isEnabled=" + isEnabled +
                ", questionAndAnswerMap=" + questionAndAnswerMap +
                ", countFailedInLatestLogin=" + countFailedInLatestLogin +
                ", joinDate=" + joinDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", activeStatus=" + activeStatus +
                ", deletedStatus=" + deletedStatus +
                ", verifyPhoneNumberStatus=" + verifyPhoneNumberStatus +
                ", verifyEmailStatus=" + verifyEmailStatus + '}';
    }
}
