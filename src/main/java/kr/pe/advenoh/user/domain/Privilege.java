package kr.pe.advenoh.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privilege_id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegeType privilegeType;

    @ManyToMany(mappedBy = "privileges")
    private List<Role> roles = new ArrayList<>();

    public Privilege(PrivilegeType privilegeType) {
        this.privilegeType = privilegeType;
    }
}