package kr.pe.advenoh.quote.repository.quote;

import kr.pe.advenoh.quote.model.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>, QuoteRepositoryCustom {
    Optional<Quote> findById(Long id);

    //todo : 이거 querydsl로 변경하면 좋을 듯함 (?)
    @Modifying
    @Query("DELETE FROM Quote q WHERE q.id IN :quoteIds")
    Integer deleteAllByQuoteIds(@Param("quoteIds") List<Long> quoteIds);

    //todo : 이것도 수정하도록 하자.
    //    @Query(value = "SELECT q FROM Quote q ORDER BY random()")
    @Query(value = "SELECT * FROM quotes ORDER BY rand() LIMIT 1", nativeQuery = true)
    Quote getRandomQuote();

    boolean existsQuoteByQuoteText(String quoteText);
}
