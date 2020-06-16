package ir.bigz.springboot.userManagement.dto;

import ir.bigz.springboot.userManagement.domain.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    List<UserApp> findAll();

    Optional<UserApp> findUserAppByEmail(String email);

    @Query(value = "select * " +
            "from user_app where phone_number = :phoneNumber",
            nativeQuery = true)
    Optional<UserApp> findUserAppByPhone(String phoneNumber);
}
