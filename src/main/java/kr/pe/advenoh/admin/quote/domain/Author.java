package kr.pe.advenoh.admin.quote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.pe.advenoh.common.model.entity.DateAudit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "quotes")
@Entity
@Table(name = "authors")
public class Author extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", unique = true, nullable = false)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Quote> quotes = new ArrayList<>();

    @Builder
    public Author(String name) {
        this.name = name;
    }
}