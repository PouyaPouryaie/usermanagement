package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.domain.ApplicationUserPermission;
import ir.bigz.springboot.userManagement.domain.ApplicationUserRole;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface ApplicationUserRoleService {

    Set<ApplicationUserPermission> getPermissionFromRoles(String roleName);

    ApplicationUserRole getApplicationUserRole(String roleName);
}
