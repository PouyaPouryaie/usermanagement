package ir.bigz.springboot.userManagement.dao;

import ir.bigz.springboot.userManagement.domain.ApplicationUser;
import ir.bigz.springboot.userManagement.domain.ApplicationUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRoleDao extends JpaRepository<ApplicationUserRole, Long> {

    Optional<ApplicationUserRole> findApplicationUserRoleByRoleName(String roleName);
}
