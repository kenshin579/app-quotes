package kr.pe.advenoh.quote.repository;

import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.FolderUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderUserMappingRepository extends JpaRepository<FolderUserMapping, Long> {
    Long deleteByFolder(Folder folder);
}
