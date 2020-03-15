package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Role;
import kr.pe.advenoh.quote.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
