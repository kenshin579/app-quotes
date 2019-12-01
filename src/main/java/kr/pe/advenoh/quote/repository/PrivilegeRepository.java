package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.Privilege;
import kr.pe.advenoh.quote.model.enums.PrivilegeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByPrivilegeType(PrivilegeType privilegeType);
}
