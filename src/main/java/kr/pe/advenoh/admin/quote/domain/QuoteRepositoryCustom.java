package kr.pe.advenoh.admin.quote.domain;

import kr.pe.advenoh.admin.quote.domain.dto.QuoteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuoteRepositoryCustom {
    Page<QuoteResponseDto> findAllByFolderId(Long folderId, Pageable pageable);

    Optional<QuoteResponseDto> findAllByQuoteId(Long quoteId);
}
