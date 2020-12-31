package kr.pe.advenoh.admin.folder.service;

import kr.pe.advenoh.admin.folder.domain.Folder;
import kr.pe.advenoh.admin.folder.domain.FolderDto;
import kr.pe.advenoh.admin.folder.domain.FolderQuoteMappingRepository;
import kr.pe.advenoh.admin.folder.domain.FolderRepository;
import kr.pe.advenoh.admin.folder.domain.FolderUserMapping;
import kr.pe.advenoh.admin.folder.domain.FolderUserMappingRepository;
import kr.pe.advenoh.admin.quote.service.QuoteService;
import kr.pe.advenoh.common.exception.ApiException;
import kr.pe.advenoh.common.exception.ErrorCode;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FolderService {
    //todo: dependency가 너무 많다. 이거 어떻게 개선하면 좋을 까?

    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final FolderUserMappingRepository folderUserMappingRepository;

    private final FolderQuoteMappingRepository folderQuoteMappingRepository;

    private final QuoteService quoteService;

    @Transactional(readOnly = true)
    public List<FolderDto.FolderResponse> getFolders(String username) {
        log.info("[debug] username : {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return folderRepository.findAllByUsername(user.getUsername());
    }

    @Transactional
    public FolderDto.FolderResponse createFolder(String folderName, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        //todo: cascade는 다시 정리하는 걸로 함
        Folder folder = new Folder(folderName);
        folderRepository.save(folder);
        FolderUserMapping folderUserMapping = new FolderUserMapping(folder, user);
        FolderUserMapping saveFolderUserMapping = folderUserMappingRepository.save(folderUserMapping);
        return new FolderDto.FolderResponse(saveFolderUserMapping.getFolder().getId(), saveFolderUserMapping.getFolder().getFolderName(), 0L);
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
        List<Long> quoteIds = folderQuoteMappingRepository.getAllQuoteIdsByFolderIds(folderIds);
        folderQuoteMappingRepository.deleteAllByFolderIds(folderIds);
        if (!quoteIds.isEmpty()) {
            quoteService.deleteQuotes(quoteIds);
        }
        folderUserMappingRepository.deleteAllByFolderIds(folderIds);
        return folderRepository.deleteAllByFolderIds(folderIds);
    }
}
