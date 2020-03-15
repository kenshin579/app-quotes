package kr.pe.advenoh.quote.spring;

import kr.pe.advenoh.quote.model.entity.Privilege;
import kr.pe.advenoh.quote.model.entity.Role;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.PrivilegeType;
import kr.pe.advenoh.quote.model.enums.RoleType;
import kr.pe.advenoh.quote.repository.PrivilegeRepository;
import kr.pe.advenoh.quote.repository.RoleRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("[springdebug] initial data load...");

        //최소 Privilege
        Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ_PRIVILEGE);
        Privilege writePrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE_PRIVILEGE);
        Privilege passwordPrivilege = createPrivilegeIfNotFound(PrivilegeType.CHANGE_PASSWORD_PRIVILEGE);

        //초기 Role
        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege);
        List<Privilege> userPrivileges = Arrays.asList(readPrivilege, passwordPrivilege);

        Role adminRole = createRoleIfNotFound(RoleType.ROLE_ADMIN, adminPrivileges);
        this.createRoleIfNotFound(RoleType.ROLE_USER, userPrivileges);

        //테스트 사용자 생성
        this.createUserIfNotFound("testuser", "test@test.com", "TestName", "testpass", Arrays.asList(adminRole));
    }

    @Transactional
    public User createUserIfNotFound(String username, String email, String name, String password, Collection<Role> roles) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            User user = new User(username, email, name, passwordEncoder.encode(password));
            user.setEnabled(true);
            user.setRoles(roles);
            return userRepository.save(user);
        });
    }

    @Transactional
    public Role createRoleIfNotFound(RoleType roleType, Collection<Privilege> privileges) {
        return roleRepository.findByRoleType(roleType).orElseGet(() -> {
            Role role = new Role(roleType);
            role.setPrivileges(privileges);
            return roleRepository.save(role);
        });
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(PrivilegeType privilegeType) {
        return privilegeRepository.findByPrivilegeType(privilegeType).orElseGet(() -> {
            Privilege privilege = new Privilege(privilegeType);
            return privilegeRepository.save(privilege);
        });
    }
}
