package kr.pe.advenoh.admin.folder.controller;

import kr.pe.advenoh.admin.folder.domain.dto.FolderDto;
import kr.pe.advenoh.admin.folder.service.FolderService;
import kr.pe.advenoh.spring.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;

    @GetMapping
    public ResponseEntity<?> getFolders(@CurrentUser Principal currentUser) {
        Map<String, Object> result = new HashMap<>();
        List<FolderDto.FolderResponse> folders = folderService.getFolders(currentUser.getName());
        FolderDto.FolderStatsResponse folderStatsResponseDto = FolderDto.FolderStatsResponse.builder()
                .totalNumOfQuotes(folders.stream().mapToLong(FolderDto.FolderResponse::getNumOfQuotes).sum())
                .totalNumOfLikes(0L)
                .build();
        result.put("folderStatInfo", folderStatsResponseDto);
        result.put("folderList", folders);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createFolder(
            @RequestParam(value = "folderName") String folderName,
            @CurrentUser Principal currentUser) {
        return new ResponseEntity<>(folderService.createFolder(folderName, currentUser.getName()), HttpStatus.OK);
    }

    @PutMapping("/{folderId}/rename")
    public ResponseEntity<?> renameFolder(
            @PathVariable(name = "folderId") Long folderId,
            @RequestParam(value = "folderName") String folderName) {
        Map<String, Object> result = new HashMap<>();
        folderService.renameFolder(folderId, folderName);
        result.put("succeed", true);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFolders(@RequestParam(value = "folderIds") List<Long> folderIds) {
        Map<String, Object> result = new HashMap<>();
        result.put("succeed", folderIds.size() == folderService.deleteFolders(folderIds));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
