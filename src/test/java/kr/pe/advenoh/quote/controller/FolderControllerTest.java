package kr.pe.advenoh.quote.controller;

import com.jayway.jsonpath.JsonPath;
import kr.pe.advenoh.quote.model.dto.SignUpRequestDto;
import kr.pe.advenoh.quote.service.IUserService;
import kr.pe.advenoh.quote.util.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FolderControllerTest {
    //todo: db가 running하고 있지 않아도 동작하도록 수정하면 좋을 듯함

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserService userService;

    private String username;

    @Before
    public void setUp() throws Exception {
        username = TestUtils.generateRandomString(5);
        log.info("username : {}", username);

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername(username);
        signUpRequestDto.setEmail("test@sdf.com");
        signUpRequestDto.setName("test");
        signUpRequestDto.setPassword("123456");
        userService.registerNewUserAccount(signUpRequestDto);
    }

    @After
    public void tearDown() throws Exception {
        userService.deleteUser(username);
    }

    @Test
    public void getFolders() throws Exception {
        this.mockMvc.perform(get("/api/folders")
                .with(user(username))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfQuotes").exists())
                .andExpect(jsonPath("$.folderStatInfo.totalNumOfLikes").exists())
                .andExpect(jsonPath("$.folderList").isArray())
                .andExpect(jsonPath("$.folderList[*].folderId").exists())
                .andExpect(jsonPath("$.folderList[*].folderName").exists())
                .andExpect(jsonPath("$.folderList[*].numOfQuotes").exists());
    }

    @Test
    @Transactional
    public void createFolder_deleteFolders_폴더에_명언이_없는_경우() throws Exception {
        //create folder
        MvcResult result = this.mockMvc.perform(post("/api/folders")
                .param("folderName", "test1")
                .with(user(username))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Integer folderId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.folderId");
        log.info("folderId : {}", folderId);

        //delete folder
        this.mockMvc.perform(delete("/api/folders")
                .param("folderIds", folderId.toString())
                .with(user(username))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}