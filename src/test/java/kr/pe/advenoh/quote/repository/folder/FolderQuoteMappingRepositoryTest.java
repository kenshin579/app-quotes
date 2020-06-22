package kr.pe.advenoh.quote.repository.folder;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FolderQuoteMappingRepositoryTest {
    @Autowired
    private FolderQuoteMappingRepository folderQuoteMappingRepository;

    @Test
    public void getAllQuoteIdsByFolderIds() {
        List<Long> allQuoteIdsByFolderIds = folderQuoteMappingRepository.getAllQuoteIdsByFolderIds(Arrays.asList(10L, 12L));
        log.info("allQuoteIdsByFolderIds : {}", allQuoteIdsByFolderIds);
    }
}