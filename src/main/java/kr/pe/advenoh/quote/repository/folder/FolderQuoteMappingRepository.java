package kr.pe.advenoh.quote.repository.folder;

import kr.pe.advenoh.quote.model.entity.FolderQuoteMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderQuoteMappingRepository extends JpaRepository<FolderQuoteMapping, Long> {
    @Modifying
    @Query("DELETE FROM FolderQuoteMapping fq WHERE fq.quote.id IN :quoteIds")
    Integer deleteByQuoteIds(@Param("quoteIds") List<Long> quoteIds);

    @Modifying
    @Query("DELETE FROM FolderQuoteMapping fq WHERE fq.folder.id IN :folderIds")
    Integer deleteAllByFolderIds(@Param("folderIds") List<Long> folderIds);

    @Query("SELECT fq.quote.id FROM FolderQuoteMapping fq WHERE fq.folder.id IN :folderIds")
    List<Long> getAllQuoteIdsByFolderIds(@Param("folderIds") List<Long> folderIds);
}