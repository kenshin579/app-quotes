package kr.pe.advenoh.quote.spring;

import kr.pe.advenoh.quote.model.Privilege;
import kr.pe.advenoh.quote.model.Role;
import kr.pe.advenoh.quote.model.User;
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
        final Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ_PRIVILEGE);
        final Privilege writePrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE_PRIVILEGE);
        final Privilege passwordPrivilege = createPrivilegeIfNotFound(PrivilegeType.CHANGE_PASSWORD_PRIVILEGE);

        //초기 Role
        final List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege);
        final List<Privilege> userPrivileges = Arrays.asList(readPrivilege, passwordPrivilege);

        final Role adminRole = createRoleIfNotFound(RoleType.ROLE_ADMIN, adminPrivileges);
        createRoleIfNotFound(RoleType.ROLE_USER, userPrivileges);

        //테스트 사용자 생성
        createUserIfNotFound("testuser", "test@test.com", "TestName", "testpass", Arrays.asList(adminRole));
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
    public Role createRoleIfNotFound(final RoleType roleType, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByRoleType(roleType);
        if (role == null) {
            role = new Role(roleType);
            role.setPrivileges(privileges);
            role = roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final PrivilegeType privilegeType) {
        Privilege privilege = privilegeRepository.findByPrivilegeType(privilegeType);
        if (privilege == null) {
            privilege = new Privilege(privilegeType);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }
}
