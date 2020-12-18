package kr.pe.advenoh.admin.quote.domain;

import kr.pe.advenoh.admin.quote.domain.enums.YN;
import kr.pe.advenoh.common.model.entity.DateAudit;
import kr.pe.advenoh.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Optional;

@Getter
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

    @Builder
    public Quote(String quoteText, Author author, User user, YN useYn) {
        this.quoteText = quoteText;
        this.author = author;
        this.user = user;
        this.useYn = useYn;
    }

    public void updateQuote(String quoteText, YN useYn, Author author) {
        Optional.ofNullable(quoteText).ifPresent(it -> this.quoteText = it);
        Optional.ofNullable(useYn).ifPresent(it -> this.useYn = it);
        this.author = author;
    }

    //todo : 편의 메서드 추가
}