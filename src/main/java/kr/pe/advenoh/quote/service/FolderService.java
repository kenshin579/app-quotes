package kr.pe.advenoh.quote.service;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.model.entity.Folder;
import kr.pe.advenoh.quote.model.entity.FolderUserMapping;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.repository.FolderRepository;
import kr.pe.advenoh.quote.repository.FolderUserMappingRepository;
import kr.pe.advenoh.quote.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderUserMappingRepository folderUserMappingRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<FolderResponseDto> getFolders(Principal currentUser) {
        log.info("[debug] currentUser : {}", currentUser);
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });
        return folderRepository.findAllByUsername(user.getUsername());
    }

    @Transactional
    public FolderResponseDto createFolder(String folderTitle, Principal currentUser) {
        User user = userRepository.findByUsername(currentUser.getName()).orElseThrow(() -> {
            throw new RuntimeException("not found");
        });

        Folder folder = new Folder(folderTitle);
        FolderUserMapping folderUserMapping = new FolderUserMapping(folder, user);
        FolderUserMapping saveFolderUserMapping = folderUserMappingRepository.save(folderUserMapping);
        return new FolderResponseDto(saveFolderUserMapping.getFolder().getId(), saveFolderUserMapping.getFolder().getFolderName(), 0L);
    }

    @Transactional
    public void updateFolder(Long folderId, String folderName) {
        Folder folder = folderRepository.getOne(folderId);
        folder.setFolderName(folderName);
    }

    @Transactional
    public Long deleteFolder(Long folderId) {
        Folder folder = folderRepository.getOne(folderId);
        return folderUserMappingRepository.deleteByFolder(folder);
    }
}
