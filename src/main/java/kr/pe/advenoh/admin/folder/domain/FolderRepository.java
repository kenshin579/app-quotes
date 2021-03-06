package kr.pe.advenoh.admin.folder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long>, FolderRepositoryCustom {
    @Modifying
    @Query("DELETE FROM Folder f WHERE f.id IN :folderIds")
    Integer deleteAllByFolderIds(@Param("folderIds") List<Long> folderIds);
}
