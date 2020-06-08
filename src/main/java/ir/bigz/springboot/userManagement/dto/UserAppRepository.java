package ir.bigz.springboot.userManagement.dto;

import ir.bigz.springboot.userManagement.domain.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    List<UserApp> findAll();
}
