package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.QuoteTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteTagMappingRepository extends JpaRepository<QuoteTagMapping, Long> {
    @Modifying
    @Query("DELETE FROM QuoteTagMapping q WHERE q.quote.id IN :quoteIds")
    Integer deleteAllByIdInQuery(@Param("quoteIds") List<Long> quoteIds);
}
