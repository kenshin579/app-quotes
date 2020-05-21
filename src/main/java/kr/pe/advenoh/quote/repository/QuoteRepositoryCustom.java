package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuoteRepositoryCustom {
    Page<Quote> findAllByFolderId(Long folderId, Pageable pageable);
}
