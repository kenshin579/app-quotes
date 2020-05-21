package kr.pe.advenoh.quote.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@ToString
@Entity
@Table(name = "quote_history")
public class QuoteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_history_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quoted_id")
    private Quote quote;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, updatable = false)
    private Date createDt;

    public QuoteHistory(Quote quote) {
        this.quote = quote;
    }
}