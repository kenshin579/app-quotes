package kr.pe.advenoh.admin.folder.domain;

import kr.pe.advenoh.util.SpringBootTestSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class FolderQuoteMappingRepositoryTest extends SpringBootTestSupport {
    @Autowired
    private FolderQuoteMappingRepository folderQuoteMappingRepository;

    @Test
    void getAllQuoteIdsByFolderIds() {
        List<Long> allQuoteIdsByFolderIds = folderQuoteMappingRepository.getAllQuoteIdsByFolderIds(Arrays.asList(1L));
        log.info("allQuoteIdsByFolderIds : {}", allQuoteIdsByFolderIds);
        assertThat(allQuoteIdsByFolderIds.size()).isEqualTo(2);
    }
}