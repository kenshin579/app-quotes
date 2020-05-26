package kr.pe.advenoh.quote.repository.folder;

import kr.pe.advenoh.quote.model.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long>, FolderRepositoryCustom {
    @Modifying
    @Query("DELETE FROM Folder f WHERE f.id IN :folderIds")
    Integer deleteAllByFolderIds(@Param("folderIds") List<Long> folderIds);
}
