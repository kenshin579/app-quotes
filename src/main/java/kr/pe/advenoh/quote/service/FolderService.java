package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.FolderUserMapping;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.repository.folder.FolderQuoteMappingRepository;
import kr.pe.advenoh.quote.repository.folder.FolderRepository;
import kr.pe.advenoh.quote.repository.folder.FolderUserMappingRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import kr.pe.advenoh.quote.repository.quote.QuoteTagMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FolderService {
    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final FolderUserMappingRepository folderUserMappingRepository;

    private final FolderQuoteMappingRepository folderQuoteMappingRepository;

    private final QuoteRepository quoteRepository;

    private final QuoteTagMappingRepository quoteTagMappingRepository;

    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<FolderResponseDto> getFolders(Principal currentUser) {
        log.info("[debug] currentUser : {}", currentUser);
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> new RuntimeException("not found"));
        return folderRepository.findAllByUsername(user.getUsername());
    }

    @Transactional
    public FolderResponseDto createFolder(String folderName, Principal currentUser) {
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> new RuntimeException("not found"));

        //todo: cascade는 다시 정리하는 걸로 함
        Folder folder = new Folder(folderName);
        folderRepository.save(folder);
        FolderUserMapping folderUserMapping = new FolderUserMapping(folder, user);
        FolderUserMapping saveFolderUserMapping = folderUserMappingRepository.save(folderUserMapping);
        return new FolderResponseDto(saveFolderUserMapping.getFolder().getId(), saveFolderUserMapping.getFolder().getFolderName(), 0L);
    }

    @Transactional
    public void renameFolder(Long folderId, String folderName) {
        Folder folder = folderRepository.getOne(folderId);
        folder.setFolderName(folderName);
    }

    /**
     * todo : 폴더에 있는 명언은 다른 곳으로 이동하도록 수정하기
     *
     * @param folderIds the folderIds
     * @return Integer
     */
    @Transactional
    public Integer deleteFolders(List<Long> folderIds) {
//        quoteRepository.deleteAllByIdInQuery(folderIds);
        //todo: quotes_tags에 있는 것도 삭제해야 함
        //todo: quotes도 삭제해야 함

        List<Long> quoteIds = folderQuoteMappingRepository.getAllQuoteIdsByFolderIds(folderIds);
        log.info("quoteIds : {}", quoteIds);

        quoteTagMappingRepository.deleteAllByQuoteIds(quoteIds);
        folderQuoteMappingRepository.deleteAllByFolderIds(folderIds);
        quoteRepository.deleteAllByQuoteIds(quoteIds);
        folderUserMappingRepository.deleteAllByFolderIds(folderIds);
        return folderRepository.deleteAllByFolderIds(folderIds);
    }
}
