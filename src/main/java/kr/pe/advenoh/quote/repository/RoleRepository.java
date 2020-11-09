package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Role;
import kr.pe.advenoh.quote.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
