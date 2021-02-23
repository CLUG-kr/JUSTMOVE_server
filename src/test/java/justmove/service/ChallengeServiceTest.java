package justmove.service;

import justmove.domain.action.ActionRepository;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.user.MockUser;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
public class ChallengeServiceTest {

    @Mock
    private Uploader s3Uploader;

    private ChallengeService challengeService;

    private User user1;

    private MockMultipartFile file = new MockMultipartFile("file", "동영상1.mp4", "file", "test".getBytes());

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActionRepository actionRepository;

    @BeforeEach
    void setUp() {
        user1 = MockUser.mockUser1();
        userRepository.save(user1);
        challengeService = new ChallengeService(s3Uploader, challengeRepository, actionRepository);
    }

    @AfterEach
    void tearDown() {
        challengeRepository.deleteAll();
        userRepository.deleteAll();
    }

//    @Test
//    @Transactional
//    public void 전달받은_dto를_가지고_s3_에_업로드_한_뒤_파일의_주소를_저장한다() throws IOException {
//        // given
//        RegisterChallengeRequestDto dto = RegisterChallengeRequestDto.builder()
//                .title("타이틀")
//                .description("내용")
//                .movie(file)
//                .tags(Collections.emptyList())
//                .build();
//        given(s3Uploader.upload(any(), any())).willReturn("movieLinkTest");
//        // when
//        String result = challengeService.registerChallenge(dto, user1);
//
//        // then
//        assertThat(result).isEqualTo("movieLinkTest");
//
//        Challenge challenge = challengeRepository.findByUploader(user1).get(0);
//        assertThat(challenge.getUploader()).isEqualTo(user1);
//        assertThat(challenge.getTitle()).isEqualTo("타이틀");
//        assertThat(challenge.getDescription()).isEqualTo("내용");
//        assertThat(challenge.getMovie().getMovieLink()).isEqualTo("movieLinkTest");
//    }

//    @Test
//    @Transactional
//    public void 전달된_태그들도_challenge와_저장한다() throws IOException {
//        // given
//        List<String> tags = new ArrayList<>();
//        tags.add("태그1");
//        tags.add("태그2");
//        RegisterChallengeRequestDto dto = RegisterChallengeRequestDto.builder()
//                .title("타이틀")
//                .description("내용")
//                .movie(file)
//                .tags(tags)
//                .build();
//        given(s3Uploader.upload(any(), any())).willReturn("movieLinkTest");
//        // when
//        challengeService.registerChallenge(dto, user1);
//
//        // then
//        Challenge challenge = challengeRepository.findByUploader(user1).get(0);
//        List<String> resultTags = challenge.getTags().stream().map(Tag::getName).collect(Collectors.toList());
//        assertThat(resultTags).isEqualTo(tags);
//    }

//    @Test
//    @Transactional
//    public void action_수가_가장_많은_순서대로_Challenge를_가져온다() {
//        // given
//        Movie movie1 = Movie.builder().movieLink("testLink1").build();
//        Challenge challenge1 =
//                Challenge.builder().title("제목1").description("내용1").movie(movie1).tags(Collections.emptyList())
//                .uploader(user1).build();
//        Movie movie2 = Movie.builder().movieLink("testLink2").build();
//        Challenge challenge2 =
//                Challenge.builder().title("제목2").description("내용2").movie(movie2).tags(Collections.emptyList())
//                .uploader(user1).build();
//        Movie movie3 = Movie.builder().movieLink("testLink3").build();
//        Challenge challenge3 =
//                Challenge.builder().title("제목3").description("내용3").movie(movie3).tags(Collections.emptyList())
//                .uploader(user1).build();
//
//        challengeRepository.save(challenge1);
//        challengeRepository.save(challenge2);
//        challengeRepository.save(challenge3);
//
//        Score score1 = Score.builder().score(100D).build();
//        Action action1 = Action.builder().user(user1).score(score1).challenge(challenge1).build();
//        Score score2 = Score.builder().score(100D).build();
//        Action action2 = Action.builder().user(user1).score(score2).challenge(challenge2).build();
//        Score score3 = Score.builder().score(100D).build();
//        Action action3 = Action.builder().user(user1).score(score3).challenge(challenge2).build();
//        Score score4 = Score.builder().score(100D).build();
//        Action action4 = Action.builder().user(user1).score(score4).challenge(challenge2).build();
//
//        actionRepository.save(action1);
//        actionRepository.save(action2);
//        actionRepository.save(action3);
//        actionRepository.save(action4);
//        // when
//        List<Challenge> challenges = challengeRepository.getPopularChallenges();
//        System.out.println(challenges);
//        // then
//        assertThat(challenges.get(0).getId()).isEqualTo(challenge2.getId());
//        assertThat(challenges.get(1).getId()).isEqualTo(challenge1.getId());
//        assertThat(challenges.get(2).getId()).isEqualTo(challenge3.getId());
//    }
}
