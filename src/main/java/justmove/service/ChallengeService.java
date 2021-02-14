package justmove.service;

import justmove.domain.challenge.Challenge;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.challenge.Movie;
import justmove.domain.tag.Tag;
import justmove.domain.user.User;
import justmove.web.dto.RegisterChallengeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final Uploader s3UploaderService;

    private final ChallengeRepository challengeRepository;

    @Transactional
    public String registerChallenge(RegisterChallengeRequestDto dto, User uploader) throws IOException {
        String fileUrl = s3UploaderService.upload(dto.getMovie(), "challenge/movie");

        Challenge challenge = Challenge.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .movie(new Movie(fileUrl))
                .uploader(uploader)
                .tags(dto.getTags().stream().map(Tag::new).collect(Collectors.toList()))
                .build();

        challengeRepository.save(challenge);

        return fileUrl;
    }

}
