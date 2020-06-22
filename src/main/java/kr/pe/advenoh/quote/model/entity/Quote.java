package kr.pe.advenoh.quote.model.entity;

import kr.pe.advenoh.quote.model.audit.DateAudit;
import kr.pe.advenoh.quote.model.enums.YN;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "quotes")
public class Quote extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id", unique = true, nullable = false)
    private Long id;

    private String quoteText;

    @Enumerated(EnumType.STRING)
    private YN useYn;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @JsonIgnore
//    @OneToMany(mappedBy = "quote")
//    private List<Like> likes = new ArrayList<>();

//    @ManyToMany(mappedBy = "quotes")
//    private Collection<Folder> folders;

//    @JsonIgnore
//    @OneToMany(mappedBy = "tag")
//    private Set<QuoteTagMapping> quoteTagMappings = new HashSet<>();

    @Builder
    public Quote(String quoteText, Author author, User user, YN useYn) {
        this.quoteText = quoteText;
        this.author = author;
        this.user = user;
        this.useYn = useYn;
    }

    //todo : 편의 메서드 추가
}