package kr.pe.advenoh.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.pe.advenoh.admin.folder.domain.FolderUserMapping;
import kr.pe.advenoh.admin.quote.domain.Like;
import kr.pe.advenoh.admin.quote.domain.Quote;
import kr.pe.advenoh.common.model.entity.DateAudit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(exclude = {"password", "roles", "quotes", "likes", "folderUserMappingList"})
@Table(name = "users")
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;

    private String username;

    private String email;

    private String name;

    @Embedded
    private Password password;

    private boolean enabled;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Quote> quotes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Like> likes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<FolderUserMapping> folderUserMappingList = new ArrayList<>();

    @Builder
    public User(String username, String email, String name, Password password, boolean enabled) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public void updateUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addRoles(List<Role> roles) {
//        this.roles.addAll(roles);
        this.roles = roles;
    }
}