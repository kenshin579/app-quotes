package kr.pe.advenoh.quote.repository.folder;

import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.FolderUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderUserMappingRepository extends JpaRepository<FolderUserMapping, Long> {
    Long deleteByFolder(Folder folder);

    @Modifying
    @Query("DELETE FROM FolderUserMapping fu WHERE fu.folder.id IN :folderIds")
    Integer deleteAllByFolderIds(@Param("folderIds") List<Long> folderIds);
}
