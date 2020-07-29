package ir.bigz.springboot.userManagement.dao;

import ir.bigz.springboot.userManagement.domain.ApplicationUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserPermissionDao extends JpaRepository<ApplicationUserPermission, Long> {
}
