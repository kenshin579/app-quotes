package kr.pe.advenoh.quote.repository.folder;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
class FolderRepositoryTest extends SpringBootTestSupport {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderUserMappingRepository folderUserMappingRepository;

    @BeforeEach
    void setUp() throws Exception {
//        Folder folder;
//        for (int i = 0; i < 3; i++) {
//            folder = new Folder("folder" + i);
//
//        }
    }

    @Test
    void findAllByUsername() {
        List<FolderResponseDto> testuser = folderRepository.findAllByUsername("testuser");
        log.info("[quotedebug] testuser : {}", testuser);
    }
}