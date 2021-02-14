package justmove.service;

import justmove.domain.challenge.Challenge;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.tag.Tag;
import justmove.domain.user.MockUser;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import justmove.web.dto.RegisterChallengeRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    @BeforeEach
    void setUp() {
        user1 = MockUser.mockUser1();
        userRepository.save(user1);
        challengeService = new ChallengeService(s3Uploader, challengeRepository);
    }

    @AfterEach
    void tearDown() {
        challengeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 전달받은_dto를_가지고_s3_에_업로드_한_뒤_파일의_주소를_저장한다() throws IOException {
        // given
        RegisterChallengeRequestDto dto = RegisterChallengeRequestDto.builder()
                .title("타이틀")
                .description("내용")
                .movie(file)
                .tags(Collections.emptyList())
                .build();
        given(s3Uploader.upload(any(), any())).willReturn("movieLinkTest");
        // when
        String result = challengeService.registerChallenge(dto, user1);

        // then
        assertThat(result).isEqualTo("movieLinkTest");

        Challenge challenge = challengeRepository.findByUploader(user1).get(0);
        assertThat(challenge.getUploader()).isEqualTo(user1);
        assertThat(challenge.getTitle()).isEqualTo("타이틀");
        assertThat(challenge.getDescription()).isEqualTo("내용");
        assertThat(challenge.getMovie().getMovieLink()).isEqualTo("movieLinkTest");
    }

    @Test
    @Transactional
    public void 전달된_태그들도_challenge와_저장한다() throws IOException {
        // given
        List<String> tags = new ArrayList<>();
        tags.add("태그1");
        tags.add("태그2");
        RegisterChallengeRequestDto dto = RegisterChallengeRequestDto.builder()
                .title("타이틀")
                .description("내용")
                .movie(file)
                .tags(tags)
                .build();
        given(s3Uploader.upload(any(), any())).willReturn("movieLinkTest");
        // when
        challengeService.registerChallenge(dto, user1);

        // then
        Challenge challenge = challengeRepository.findByUploader(user1).get(0);
        List<String> resultTags = challenge.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        assertThat(resultTags).isEqualTo(tags);
    }
}
