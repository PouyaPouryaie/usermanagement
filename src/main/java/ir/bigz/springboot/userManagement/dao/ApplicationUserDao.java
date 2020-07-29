package ir.bigz.springboot.userManagement.dao;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserDao extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findApplicationUserByUserName(String userName);

    Optional<ApplicationUser> findUserAppByEmail(String email);

    @Query(value = "select * " +
            "from application_user where phone_number = :phoneNumber",
            nativeQuery = true)
    Optional<ApplicationUser> findUserAppByPhone(String phoneNumber);
}
