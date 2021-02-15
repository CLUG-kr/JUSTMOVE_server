package justmove.service;

import justmove.domain.action.Action;
import justmove.domain.challenge.Challenge;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.challenge.Movie;
import justmove.domain.user.MockUser;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import justmove.service.exception.ChallengeNotFoundException;
import justmove.web.dto.RegisterActionDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ActionServiceTest {

    private User user1;
    private User user2;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActionService actionService;

    @BeforeEach
    void setUp() {
        user1 = MockUser.mockUser1();
        user2 = MockUser.mockUser1();
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        challengeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 전달된_도전이_존재하지_않으면_ChallengeNotFoundException을_던진다() {
        // given
        RegisterActionDto dto = RegisterActionDto.builder().score(100D).build();
        // when
        assertThatThrownBy(() -> actionService.registerAction(user1, 1234L, dto)).isExactlyInstanceOf(ChallengeNotFoundException.class);
        // then
    }

    @Test
    @Transactional
    public void 정상적으로_도전이_전달되면_도전을_저장하고_사용자와_도전에_대한_연관_관계를_설정한다() {
        // given
        Movie movie = Movie.builder().movieLink("testLink").build();
        Challenge challenge = Challenge.builder().uploader(user2).movie(movie).tags(Collections.emptyList()).title(
                "제목").description("설명").build();
        challengeRepository.save(challenge);
        RegisterActionDto dto = RegisterActionDto.builder().score(100D).build();
        // when
        Action resultAction = actionService.registerAction(user1, challenge.getId(), dto);
        // then
        User resultUser = userRepository.findById(user1.getId()).get();
        assertThat(resultUser.getActions().contains(resultAction)).isTrue();

        Challenge resultChallenge = challengeRepository.findById(challenge.getId()).get();
        assertThat(resultChallenge.getActions().contains(resultAction)).isTrue();
    }
}
