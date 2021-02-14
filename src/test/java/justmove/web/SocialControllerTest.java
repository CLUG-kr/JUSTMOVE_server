package justmove.web;

import justmove.config.auth.dto.SessionUser;
import justmove.domain.user.MockUser;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SocialControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private MockHttpSession session;

    private User user1;
    private User user2;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user1 = MockUser.mockUser1();
        user2 = MockUser.mockUser2();
        userRepository.save(user1);
        userRepository.save(user2);
        session = new MockHttpSession();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        session.clearAttributes();
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    @WithAnonymousUser
    void 로그인하지_않은_유저가_follow나_unfollow를_시도하면_401_를_반환한다() throws Exception {
        // given
        String url = getUrl() + "/social/follow/" + user2.getId();
        // when
        mvc.perform(post(url).session(session).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());

        // then
    }

    @Test
    @Transactional
    @WithMockUser
    void 로그인한_유저가_전달된_유저를_follow한다() throws Exception {
        // given
        String url = getUrl() + "/social/follow/" + user2.getId();
        session.setAttribute("user", new SessionUser(user1));
        // when
        mvc.perform(post(url).session(session).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        // then
        User resultUser1 = userRepository.findById(user1.getId()).get();
        User resultUser2 = userRepository.findById(user2.getId()).get();

        assertThat(resultUser1.getFollowings().contains(user2)).isTrue();
        assertThat(resultUser2.getFollowers().contains(user1)).isTrue();
    }

    @Test
    @Transactional
    @WithMockUser
    void 로그인한_유저가_전달된_유저를_unfollow한다() throws Exception {
        // given
        String url = getUrl() + "/social/unfollow/" + user2.getId();
        session.setAttribute("user", new SessionUser(user1));
        // 레포지터리 호출 안해도 DB에 반영되는 부분 확인했음
        user1.follow(user2);
        // when
        mvc.perform(post(url).session(session).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted());

        // then
        User resultUser1 = userRepository.findById(user1.getId()).get();
        User resultUser2 = userRepository.findById(user2.getId()).get();

        assertThat(resultUser1.getFollowings().contains(user2)).isFalse();
        assertThat(resultUser2.getFollowers().contains(user1)).isFalse();
    }

    @Test
    @Transactional
    @WithMockUser
    void 전달된_유저가_존재하지_않는다면_404_를_반환한다() throws Exception {
        // given
        String url1 = getUrl() + "/social/follow/" + 10000;
        String url2 = getUrl() + "/social/unfollow/" + 10000;
        session.setAttribute("user", new SessionUser(user1));
        // when
        mvc.perform(post(url1).session(session).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
        mvc.perform(post(url2).session(session).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

        // then
    }
}
