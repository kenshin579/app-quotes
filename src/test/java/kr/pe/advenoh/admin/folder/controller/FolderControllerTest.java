package kr.pe.advenoh.admin.folder.controller;

import com.jayway.jsonpath.JsonPath;
import kr.pe.advenoh.spring.InitialDataLoader;
import kr.pe.advenoh.user.domain.AccountDto;
import kr.pe.advenoh.user.domain.Privilege;
import kr.pe.advenoh.user.domain.PrivilegeType;
import kr.pe.advenoh.user.domain.Role;
import kr.pe.advenoh.user.domain.RoleType;
import kr.pe.advenoh.user.domain.User;
import kr.pe.advenoh.util.SpringMockMvcTestSupport;
import kr.pe.advenoh.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class FolderControllerTest extends SpringMockMvcTestSupport {
    private final String BASE_URL = "/api/folders";
    private String folderName;

    @BeforeEach
    void setUp() {
        folderName = TestUtils.generateRandomString(3);
    }

    @Test
    @Transactional
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void createFolder_getFolder() throws Exception {
        //create folder
        ResultActions resultActions = requestCreateFolder();
        resultActions.andExpect(status().isOk());

        Integer folderId = JsonPath.parse(resultActions.andReturn().getResponse().getContentAsString()).read("$.folderId");
        log.info("folderId : {}", folderId);

        //get folders
        requestGetFolders()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfQuotes").exists())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfLikes").exists())
                .andExpect(jsonPath("$.folderList").isArray())
                .andExpect(jsonPath("$.folderList[1].folderId", is(folderId)));
    }

    @Test
    @Transactional
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void createFolder_renameFolder() throws Exception {
        //create folder
        ResultActions resultActions = requestCreateFolder();

        Integer folderId = JsonPath.parse(resultActions.andReturn().getResponse().getContentAsString()).read("$.folderId");
        log.info("folderId : {}", folderId);

        //rename folder
        String newFolderName = "new " + folderName;
        this.mockMvc.perform(put(BASE_URL + "/{folderId}/rename", folderId)
                .param("folderName", newFolderName)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeed", is(true)));

        //get folders
        requestGetFolders()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfQuotes").exists())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfLikes").exists())
                .andExpect(jsonPath("$.folderList[1].folderName", is(newFolderName)));
    }

    private ResultActions requestCreateFolder() throws Exception {
        return this.mockMvc.perform(post(BASE_URL)
                .param("folderName", this.folderName)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestGetFolders() throws Exception {
        return this.mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}