package kr.pe.advenoh.quote.repository.quote;

import kr.pe.advenoh.quote.model.dto.QuoteResponseDto;
import kr.pe.advenoh.quote.model.entity.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuoteRepositoryCustom {
    Page<Quote> findAllByFolderId(Long folderId, Pageable pageable);

    Optional<QuoteResponseDto> findAllByQuoteId(Long quoteId);
}
