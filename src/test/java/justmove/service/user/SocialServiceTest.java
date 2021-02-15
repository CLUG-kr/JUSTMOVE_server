package justmove.service.user;

import justmove.domain.challenge.Challenge;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.challenge.Movie;
import justmove.domain.user.MockUser;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import justmove.service.SocialService;
import justmove.service.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest()
public class SocialServiceTest {

    private User user1;
    private User user2;
    private User user3;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private SocialService socialService;

    @BeforeEach
    public void setUp() {
        user1 = MockUser.mockUser1();
        user2 = MockUser.mockUser2();
        user3 = MockUser.mockUser3();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @AfterEach
    public void tearDown() {
        challengeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void a_유저가_b_유저를_follow하면_a는_b를_following으로_b는_a를_follower로_가지게_된다() {
        // given

        // when
        socialService.follow(user1.getId(), user2.getId());

        // then
        User fromUser = userRepository.findById(user1.getId()).get();
        User targetUser = userRepository.findById(user2.getId()).get();

        assertThat(fromUser.getFollowings().contains(user2)).isTrue();
        assertThat(targetUser.getFollowers().contains(user1)).isTrue();
    }

    @Test
    @Transactional
    public void a_유저가_b_유저를_unfollow하면_a의_following에서_b가_사라지고_b의_follower에서_a가_사라진다() {
        // given
        socialService.follow(user1.getId(), user2.getId());
        socialService.follow(user1.getId(), user3.getId());
        socialService.follow(user2.getId(), user3.getId());
        socialService.follow(user3.getId(), user2.getId());
        // when
        socialService.unfollow(user1.getId(), user2.getId());

        // then
        User resultUser1 = userRepository.findById(user1.getId()).get();
        User resultUser2 = userRepository.findById(user2.getId()).get();
        User resultUser3 = userRepository.findById(user3.getId()).get();

        assertThat(resultUser1.getFollowings().contains(user2)).isFalse();
        assertThat(resultUser1.getFollowings().contains(user3)).isTrue();

        assertThat(resultUser2.getFollowers().contains(user1)).isFalse();
        assertThat(resultUser2.getFollowers().contains(user3)).isTrue();

        assertThat(resultUser3.getFollowers().contains(user1)).isTrue();
        assertThat(resultUser3.getFollowings().contains(user2)).isTrue();
    }

    @Test
    @Transactional
    public void 자기_자신을_follow할_수_없으므로_아무런_일도_일어나지_않는다() {
        // given

        // when
        socialService.follow(user1.getId(), user1.getId());

        // then
        User resultUser1 = userRepository.findById(user1.getId()).get();

        assertThat(resultUser1.getFollowings().size()).isEqualTo(0);
        assertThat(resultUser1.getFollowers().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void 존재하지_않는_유저가_또는_유저를_follow하거나_unfollow하려고_하면_user_not_found_exception를_던진다() {
        // given

        // when

        // then
        assertThatThrownBy(() -> socialService.follow(user1.getId(), 100L)).isExactlyInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> socialService.follow(100L, user1.getId())).isExactlyInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> socialService.follow(100L, 200L)).isExactlyInstanceOf(UserNotFoundException.class);
    }

    @Test
    @Transactional
    public void 팔로잉한_사람들의_challenge를_최신순_리스트로_가져온다() {
        // given
        user1.follow(user2);
        Movie movie1 = Movie.builder().movieLink("testLink1").build();
        Challenge challenge1 =
                Challenge.builder().title("제목1").description("내용1").movie(movie1).tags(Collections.emptyList()).uploader(user2).build();
        Movie movie2 = Movie.builder().movieLink("testLink2").build();
        Challenge challenge2 =
                Challenge.builder().title("제목2").description("내용2").movie(movie2).tags(Collections.emptyList()).uploader(user2).build();

        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);
        // when
        List<Challenge> challenges = socialService.getFollowingChallenges(user1);
        // then
        assertThat(challenges.get(0).getId()).isEqualTo(challenge2.getId());
        assertThat(challenges.get(1).getId()).isEqualTo(challenge1.getId());
    }
}
