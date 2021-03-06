package ir.bigz.springboot.userManagement.service;

import ir.bigz.springboot.userManagement.dao.ApplicationUserRoleDao;
import ir.bigz.springboot.userManagement.domain.ApplicationUserPermission;
import ir.bigz.springboot.userManagement.domain.ApplicationUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Component("ApplicationUserRoleServiceImpl")
@Slf4j
public class ApplicationUserRoleServiceImpl implements ApplicationUserRoleService{

    private final ApplicationUserRoleDao applicationUserRoleDao;

    public ApplicationUserRoleServiceImpl(ApplicationUserRoleDao applicationUserRoleDao) {
        this.applicationUserRoleDao = applicationUserRoleDao;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
    public Set<ApplicationUserPermission> getPermissionFromRoles(String roleName) {

        try {
            ApplicationUserRole applicationUserRole = getApplicationUserRole(roleName);
            return applicationUserRole.getApplicationUserPermissionsForRole();
        }catch (Exception e){
            log.error(String.format("getPermissionFromRoles method has error \n" + e.getMessage()));
        }

        return Collections.emptySet();
    }

    @Override
    public ApplicationUserRole getApplicationUserRole(String roleName){

        Optional<ApplicationUserRole> applicationUserRole = applicationUserRoleDao
                .findApplicationUserRoleByRoleName(roleName);

        if(applicationUserRole.isPresent()){
            return applicationUserRole.get();
        }else {
            throw new IllegalStateException(String.format("role by %s not found", roleName));
        }

    }
}
