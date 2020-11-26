package kr.pe.advenoh.admin.folder.service;

import kr.pe.advenoh.admin.folder.domain.FolderQuoteMappingRepository;
import kr.pe.advenoh.admin.folder.domain.FolderRepository;
import kr.pe.advenoh.admin.folder.domain.FolderUserMappingRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteHistoryRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteRepository;
import kr.pe.advenoh.admin.quote.domain.QuoteTagMappingRepository;
import kr.pe.advenoh.util.MockitoTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FolderServiceMockTest extends MockitoTestSupport {
    @InjectMocks
    private FolderService folderService;

    @Mock
    private FolderQuoteMappingRepository folderQuoteMappingRepository;

    @Mock
    private QuoteTagMappingRepository quoteTagMappingRepository;

    @Mock
    private QuoteHistoryRepository quoteHistoryRepository;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private FolderUserMappingRepository folderUserMappingRepository;

    @Mock
    private FolderRepository folderRepository;

    @Test
    void deleteFolders_folder안에_명언이_없는_경우() {
        //given
        List<Long> folderIds = Arrays.asList(1L);
        //		List<Long> quoteIds = Arrays.asList(10L, 20L);
        when(folderQuoteMappingRepository.getAllQuoteIdsByFolderIds(folderIds)).thenReturn(Collections.EMPTY_LIST);

        //when
        folderService.deleteFolders(folderIds);

        //then
        verify(quoteRepository, never()).deleteAllByQuoteIds(anyList());
        verify(folderQuoteMappingRepository).deleteAllByFolderIds(folderIds);
        verify(folderUserMappingRepository).deleteAllByFolderIds(folderIds);
        verify(folderRepository).deleteAllByFolderIds(folderIds);
        verify(folderQuoteMappingRepository).getAllQuoteIdsByFolderIds(anyList());
    }

    @Test
    void deleteFolders_folder안에_명언이_있는_경우() {
        //given
        List<Long> folderIds = Arrays.asList(1L);
        List<Long> quoteIds = Arrays.asList(10L, 20L);
        when(folderQuoteMappingRepository.getAllQuoteIdsByFolderIds(folderIds)).thenReturn(quoteIds);

        //when
        folderService.deleteFolders(folderIds);

        //then
        verify(folderQuoteMappingRepository).deleteAllByFolderIds(folderIds);

        verify(quoteTagMappingRepository).deleteAllByQuoteIds(quoteIds);
        verify(quoteHistoryRepository).deleteAllByQuoteIds(quoteIds);
        verify(quoteRepository).deleteAllByQuoteIds(quoteIds);

        verify(folderUserMappingRepository).deleteAllByFolderIds(folderIds);
        verify(folderRepository).deleteAllByFolderIds(folderIds);
        verify(folderQuoteMappingRepository).getAllQuoteIdsByFolderIds(anyList());
    }
}