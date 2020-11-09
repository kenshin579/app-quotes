package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Privilege;
import kr.pe.advenoh.quote.model.enums.PrivilegeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByPrivilegeType(PrivilegeType privilegeType);
}
