package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.Role;
import kr.pe.advenoh.quote.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(RoleType roleType);
}
