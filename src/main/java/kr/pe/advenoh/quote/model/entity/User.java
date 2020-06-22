package kr.pe.advenoh.quote.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.pe.advenoh.quote.model.audit.DateAudit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString(exclude = {"password", "roles", "quotes", "likes", "folderUserMappingList"})
@NoArgsConstructor
@Table(name = "users")
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;

    private String username;

    private String email;

    private String name;

    private String password;

    private boolean enabled;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "quotes_tags",
//            joinColumns = @JoinColumn(name = "quote_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id"))
//    private Set<Tag> tags = new HashSet<>();
//

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
    public User(String username, String email, String name, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}