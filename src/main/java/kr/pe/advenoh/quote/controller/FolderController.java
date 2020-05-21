package kr.pe.advenoh.quote.controller;

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
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/folders")
public class FolderController {
    @Autowired
    private FolderService folderService;

    //todo : 폴더 별로 몇개의 명언이 있는지에 대한 통계로 내려주도록 함
    @GetMapping
    public Object getFolders(@CurrentUser Principal currentUser) {
        return folderService.getFolders(currentUser);
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
