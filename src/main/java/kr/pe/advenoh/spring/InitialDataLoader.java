package kr.pe.advenoh.spring;

import kr.pe.advenoh.user.domain.AccountDto;
import kr.pe.advenoh.user.domain.Privilege;
import kr.pe.advenoh.user.domain.PrivilegeRepository;
import kr.pe.advenoh.user.domain.PrivilegeType;
import kr.pe.advenoh.user.domain.Role;
import kr.pe.advenoh.user.domain.RoleRepository;
import kr.pe.advenoh.user.domain.RoleType;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

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
//        this.createUserIfNotFound("testuser", "test@test.com", "Test", "testpass", Arrays.asList(adminRole));
    }

    @Transactional
    public User createUserIfNotFound(AccountDto.SignUpRequestDto requestNewUser, List<Role> roles) {
        return userRepository.findByUsername(requestNewUser.getUsername()).orElseGet(() -> {
            User user = requestNewUser.toEntity();
            user.addRoles(roles);
            return userRepository.save(user);
        });
    }

    @Transactional
    public Role createRoleIfNotFound(RoleType roleType, List<Privilege> privileges) {
        return roleRepository.findByRoleType(roleType).orElseGet(() -> {
            Role role = new Role(roleType);
            role.addPrivilege(privileges);
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
