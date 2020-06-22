package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Privilege;
import kr.pe.advenoh.quote.model.enums.PrivilegeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByPrivilegeType(PrivilegeType privilegeType);
}
