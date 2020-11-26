package kr.pe.advenoh.admin.quote.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {
    @Modifying
    @Query("DELETE FROM QuoteHistory qh WHERE qh.quote.id IN :quoteIds")
    Integer deleteAllByQuoteIds(@Param("quoteIds") List<Long> quoteIds);
}
