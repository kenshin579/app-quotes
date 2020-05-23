package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.FolderUserMapping;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.repository.FolderQuoteMappingRepository;
import kr.pe.advenoh.quote.repository.FolderRepository;
import kr.pe.advenoh.quote.repository.FolderUserMappingRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private final ModelMapper modelMapper;

    public List<FolderResponseDto> getFolders(Principal currentUser) {
        log.info("[debug] currentUser : {}", currentUser);
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });
        return folderRepository.findAllByUsername(user.getUsername());
    }

    @Transactional
    public FolderResponseDto createFolder(String folderName, Principal currentUser) {
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        //todo: cascade는 다시 정리하는 걸로 함
        Folder folder = new Folder(folderName);
        folderRepository.save(folder);
        FolderUserMapping folderUserMapping = new FolderUserMapping(folder, user);
        FolderUserMapping saveFolderUserMapping = folderUserMappingRepository.save(folderUserMapping);
        return new FolderResponseDto(saveFolderUserMapping.getFolder().getId(), saveFolderUserMapping.getFolder().getFolderName(), 0L);
    }

    @Transactional
    public void updateFolder(Long folderId, String folderName) {
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
        folderUserMappingRepository.deleteAllByIdInQuery(folderIds);
        folderQuoteMappingRepository.deleteByQuoteIdQuery(folderIds);
        return folderRepository.deleteAllByIdInQuery(folderIds);
    }
}
