package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.repository.folder.FolderQuoteMappingRepository;
import kr.pe.advenoh.quote.repository.folder.FolderRepository;
import kr.pe.advenoh.quote.repository.folder.FolderUserMappingRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteHistoryRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteTagMappingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FolderServiceTest {
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
	public void deleteFolders_folder안에_명언이_없는_경우() {
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
	public void deleteFolders_folder안에_명언이_있는_경우() {
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