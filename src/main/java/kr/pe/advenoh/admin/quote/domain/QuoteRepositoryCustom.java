package kr.pe.advenoh.admin.quote.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuoteRepositoryCustom {
    Page<QuoteDto.QuoteResponse> findAllByFolderId(Long folderId, Pageable pageable);

    Optional<QuoteDto.QuoteResponse> findAllByQuoteId(Long quoteId);
}
