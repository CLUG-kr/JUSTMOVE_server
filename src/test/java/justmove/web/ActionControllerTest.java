package justmove.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import justmove.config.auth.dto.SessionUser;
import justmove.domain.action.Action;
import justmove.domain.action.Score;
import justmove.domain.challenge.Challenge;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.challenge.Movie;
import justmove.domain.user.MockUser;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import justmove.service.ActionService;
import justmove.service.exception.ChallengeNotFoundException;
import justmove.web.dto.RegisterActionDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ActionControllerTest {

    private final RegisterActionDto requestDto = RegisterActionDto.builder().score(100D).build();

    @LocalServerPort
    private int port;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private MockHttpSession session;

    @Autowired
    private ObjectMapper objectMapper;

    private Challenge challenge;
    private Movie movie;
    private Action action;
    private Score score;

    private User user1;
    private User user2;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @MockBean
    private ActionService actionService;

    @BeforeEach
    void setUp() {
        user1 = MockUser.mockUser1();
        user2 = MockUser.mockUser2();
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        movie = Movie.builder().movieLink("testLink").build();
        challenge =
                Challenge.builder().title("제목").description("설명").uploader(user2).tags(Collections.emptyList()).movie(movie).build();
        challengeRepository.save(challenge);

        score = Score.builder().score(100D).build();
        action = Action.builder().challenge(challenge).score(score).user(user1).build();

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
        challengeRepository.deleteAll();
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    @WithMockUser
    void 업로드에_성공하면_201_을_반환한다() throws Exception {
        // given
        session.setAttribute("user", new SessionUser(user1));
        String url = getUrl() + "/action/challenge/" + challenge.getId();
        String content = objectMapper.writeValueAsString(requestDto);

        given(actionService.registerAction(any(), eq(challenge.getId()), any())).willReturn(action);
        // when
        mvc.perform(post(url).session(session).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

        // then
    }

    @Test
    @WithMockUser
    void challenge가_존재하지_않으면_404_를_던진다() throws Exception {
        // given
        session.setAttribute("user", new SessionUser(user1));
        String url = getUrl() + "/action/challenge/12312312";
        String content = objectMapper.writeValueAsString(requestDto);

        given(actionService.registerAction(any(), eq(12312312L), any())).willThrow(ChallengeNotFoundException.class);
        // when
        mvc.perform(post(url).session(session).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isNotFound());

        // then
    }
}
