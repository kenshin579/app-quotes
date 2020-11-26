package kr.pe.advenoh.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByPrivilegeType(PrivilegeType privilegeType);
}
