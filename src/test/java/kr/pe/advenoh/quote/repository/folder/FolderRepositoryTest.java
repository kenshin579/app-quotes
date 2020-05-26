package kr.pe.advenoh.quote.repository.folder;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.repository.folder.FolderRepository;
import kr.pe.advenoh.quote.repository.folder.FolderUserMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
//@Import({JpaConfig.class, AuditingConfig.class})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FolderRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderUserMappingRepository folderUserMappingRepository;

    @Before
    public void setUp() throws Exception {
//        Folder folder;
//        for (int i = 0; i < 3; i++) {
//            folder = new Folder("folder" + i);
//
//        }
    }

    @Test
    public void findAllByUsername() {
        List<FolderResponseDto> testuser = folderRepository.findAllByUsername("testuser");
        log.info("[quotedebug] testuser : {}", testuser);
    }
}