package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.FolderQuoteMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderQuoteMappingRepository extends JpaRepository<FolderQuoteMapping, Long> {
    @Modifying
    @Query("DELETE FROM FolderQuoteMapping fq WHERE fq.quote.id IN :quoteIds")
    Integer deleteByQuoteIdQuery(@Param("quoteIds") List<Long> quoteIds);
}