package kr.pe.advenoh.quote.repository.quote;

import kr.pe.advenoh.quote.model.entity.QuoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {
}
