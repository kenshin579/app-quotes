package kr.pe.advenoh.quote.model;

import kr.pe.advenoh.quote.model.enums.PrivilegeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privilege_id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegeType name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
}