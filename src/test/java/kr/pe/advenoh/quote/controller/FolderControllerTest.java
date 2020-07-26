package kr.pe.advenoh.quote.controller;

import com.jayway.jsonpath.JsonPath;
import kr.pe.advenoh.quote.exception.ApiException;
import kr.pe.advenoh.quote.exception.QuoteExceptionCode;
import kr.pe.advenoh.quote.model.entity.Role;
import kr.pe.advenoh.quote.model.entity.User;
import kr.pe.advenoh.quote.model.enums.RoleType;
import kr.pe.advenoh.quote.repository.RoleRepository;
import kr.pe.advenoh.quote.spring.InitialDataLoader;
import kr.pe.advenoh.quote.util.SpringMockMvcTestSupport;
import kr.pe.advenoh.quote.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class FolderControllerTest extends SpringMockMvcTestSupport {
    private User user;
    private String folderName;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InitialDataLoader initialDataLoader;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.findByRoleType(RoleType.ROLE_USER).orElseThrow(() ->
                new ApiException(QuoteExceptionCode.ACCOUNT_ROLE_NOT_FOUND, RoleType.ROLE_USER.name()));
        user = initialDataLoader.createUserIfNotFound(username, email, name, password, Arrays.asList(role));
        folderName = TestUtils.generateRandomString(3);
        log.info("user: {} folderName : {}", user, folderName);
    }

    @Test
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void getFolders() throws Exception {
        this.mockMvc.perform(get("/api/folders")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfQuotes").exists())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfLikes").exists())
                .andExpect(jsonPath("$.folderList").isArray());
    }

    @Test
    @Transactional
    @WithMockUser(username = username, authorities = {ROLE_USER})
    void createFolder_renameFolder_deleteFolders() throws Exception {
        //create folder
        MvcResult result = this.mockMvc.perform(post("/api/folders")
                .param("folderName", folderName)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Integer folderId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.folderId");
        log.info("folderId : {}", folderId);

        //rename folder
        String newFolderName = "new " + folderName;
        this.mockMvc.perform(put("/api/folders/{folderId}/rename", folderId)
                .param("folderName", newFolderName)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeed", is(true)));

        //delete folder
        this.mockMvc.perform(delete("/api/folders")
                .param("folderIds", folderId.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeed", is(true)));
    }
}