package justmove.web;

import com.amazonaws.SdkClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import justmove.config.auth.dto.SessionUser;
import justmove.domain.user.MockUser;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import justmove.service.ChallengeService;
import justmove.web.dto.RegisterChallengeRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeControllerTest {

    private final MockMultipartFile file = new MockMultipartFile("file", "동영상1.mp4", "file", "test".getBytes());
    private final RegisterChallengeRequestDto requestDto = RegisterChallengeRequestDto.builder()
            .title("타이틀")
            .description("내용")
            .tags(Collections.emptyList())
            .build();
    @LocalServerPort
    private int port;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mvc;
    private MockHttpSession session;
    private User user1;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private ChallengeService challengeService;

    @BeforeEach
    void setUp() {
        user1 = MockUser.mockUser1();
        userRepository.save(user1);
        session = new MockHttpSession();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        session.clearAttributes();
        userRepository.deleteAll();
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    @WithMockUser
    void 업로드에_성공하면_201_을_반환한다() throws Exception {
        // given
        session.setAttribute("user", new SessionUser(user1));
        String url = getUrl() + "/challenge";
        String content = objectMapper.writeValueAsString(requestDto);

        given(challengeService.registerChallenge(any(), any())).willReturn("fileUrl");
        // when
        mvc.perform(multipart(url).file(file).session(session).content(content)).andExpect(status().isCreated());

        // then
    }

    @Test
    @WithMockUser
    void S_3_업로드_과정에서_문제가_생긴_경우_503_을_반환한다() throws Exception {
        // given
        session.setAttribute("user", new SessionUser(user1));
        String url = getUrl() + "/challenge";
        String content = objectMapper.writeValueAsString(requestDto);

        given(challengeService.registerChallenge(any(), any())).willThrow(SdkClientException.class);
        // when
        mvc.perform(multipart(url).file(file).session(session).content(content)).andExpect(status().isServiceUnavailable());

        // then
    }
}
