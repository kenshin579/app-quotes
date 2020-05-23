package kr.pe.advenoh.quote.controller;

import kr.pe.advenoh.quote.model.dto.FolderResponseDto;
import kr.pe.advenoh.quote.model.dto.FolderStatsResponseDto;
import kr.pe.advenoh.quote.service.FolderService;
import kr.pe.advenoh.quote.spring.security.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/api/folders")
public class FolderController {
    @Autowired
    private FolderService folderService;

    @GetMapping
    public Object getFolders(@CurrentUser Principal currentUser) {
        Map<String, Object> result = new HashMap<>();
        List<FolderResponseDto> folders = folderService.getFolders(currentUser);
        FolderStatsResponseDto folderStatsResponseDto = FolderStatsResponseDto.builder()
                .totalNumOfQuotes(folders.stream().mapToLong(FolderResponseDto::getNumOfQuotes).sum())
                .totalNumOfLikes(0L)
                .build();
        result.put("folderStatInfo", folderStatsResponseDto);
        result.put("folderList", folders);
        return result;
    }

    @PostMapping
    public Object createFolder(
            @RequestParam(value = "folderName") String folderName,
            @CurrentUser Principal currentUser) {
        return folderService.createFolder(folderName, currentUser);
    }

    @PutMapping("/{folderId}")
    public Object updateFolder(
            @PathVariable(name = "folderId") Long folderId,
            @RequestParam(value = "folderName") String folderName) {
        Map<String, Object> result = new HashMap<>();
        folderService.updateFolder(folderId, folderName);
        result.put("succeed", true);
        return result;
    }

    @DeleteMapping("/{folderId}")
    public Object deleteFolder(@PathVariable Long folderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("succeed", 1 == folderService.deleteFolder(folderId));
        return result;
    }
}
