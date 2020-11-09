package kr.pe.advenoh.quote.repository.quote;

import kr.pe.advenoh.quote.model.entity.QuoteTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuoteTagMappingRepository extends JpaRepository<QuoteTagMapping, Long> {
    @Modifying
    @Query("DELETE FROM QuoteTagMapping q WHERE q.quote.id IN :quoteIds")
    Integer deleteAllByQuoteIds(@Param("quoteIds") List<Long> quoteIds);

    @Query("SELECT qt FROM QuoteTagMapping qt WHERE qt.quote.id in :quoteIds")
    List<QuoteTagMapping> findAllByQuoteIds(@Param("quoteIds") List<Long> quoteIds);
}