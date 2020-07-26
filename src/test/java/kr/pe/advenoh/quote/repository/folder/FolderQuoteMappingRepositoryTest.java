package kr.pe.advenoh.quote.repository.folder;

import kr.pe.advenoh.quote.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Slf4j
class FolderQuoteMappingRepositoryTest extends SpringBootTestSupport {
    @Autowired
    private FolderQuoteMappingRepository folderQuoteMappingRepository;

    @Test
    void getAllQuoteIdsByFolderIds() {
        List<Long> allQuoteIdsByFolderIds = folderQuoteMappingRepository.getAllQuoteIdsByFolderIds(Arrays.asList(10L, 12L));
        log.info("allQuoteIdsByFolderIds : {}", allQuoteIdsByFolderIds);
    }
}