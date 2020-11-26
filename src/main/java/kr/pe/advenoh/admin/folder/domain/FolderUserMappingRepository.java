package kr.pe.advenoh.admin.folder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderUserMappingRepository extends JpaRepository<FolderUserMapping, Long> {
    Long deleteByFolder(Folder folder);

    @Modifying
    @Query("DELETE FROM FolderUserMapping fu WHERE fu.folder.id IN :folderIds")
    Integer deleteAllByFolderIds(@Param("folderIds") List<Long> folderIds);
}
