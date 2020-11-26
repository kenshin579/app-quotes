package kr.pe.advenoh.admin.folder.domain;

import kr.pe.advenoh.admin.quote.domain.Quote;
import kr.pe.advenoh.common.entity.audit.DateAudit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "folders_quotes")
public class FolderQuoteMapping extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_quote_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "quote_id")
    private Quote quote;

    public FolderQuoteMapping(Folder folder, Quote quote) {
        this.folder = folder;
        this.quote = quote;
    }
}
