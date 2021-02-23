package justmove.service;

import justmove.domain.action.Action;
import justmove.domain.action.ActionRepository;
import justmove.domain.challenge.Challenge;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.challenge.Movie;
import justmove.domain.tag.Tag;
import justmove.domain.user.User;
import justmove.service.exception.ChallengeNotFoundException;
import justmove.web.dto.ChallengeInfoDto;
import justmove.web.dto.RankingDto;
import justmove.web.dto.RegisterChallengeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final Uploader s3UploaderService;

    private final ChallengeRepository challengeRepository;
    private final ActionRepository actionRepository;

    @Transactional
    public String registerChallenge(RegisterChallengeRequestDto dto, User uploader) throws IOException {
        String fileUrl = s3UploaderService.upload(dto.getMovie(), "challenge/movie");

        Challenge challenge = Challenge.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .movie(new Movie(fileUrl))
//                .uploader(uploader)
                .tags(dto.getTags().stream().map(Tag::new).collect(Collectors.toList()))
                .build();

        challengeRepository.save(challenge);

        return fileUrl;
    }

    @Transactional(readOnly = true)
    public List<Challenge> getPopularChallenges() {
        return challengeRepository.getPopularChallenges();
    }

    @Transactional(readOnly = true)
    public ChallengeInfoDto getChallengeInfo(Long challengeId) {
        Challenge challenge =
                challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeNotFoundException(
                        "challengeId : " + challengeId));
//        Action action = actionRepository.findByUserAndChallenge(actionUser, challenge).orElse(null);
//        Double actionScore = action == null ? null : action.getScore().getScore();

        List<Action> actions = actionRepository.findByChallenge(challenge);
        List<RankingDto> rankings = new ArrayList<>();
        Double scoreSum = 0D;
        for (Action e : actions) {
            rankings.add(new RankingDto(e.getUserName(), e.getScore().getScore()));
            scoreSum += e.getScore().getScore();
        }

        Double averageScore = rankings.size() == 0 ? 0 : scoreSum / rankings.size();

        return ChallengeInfoDto.builder()
                .challengeTitle(challenge.getTitle())
                .challengeDescription(challenge.getDescription())
                .tags(challenge.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .rankings(rankings)
                .averageScore(averageScore)
                .build();
    }
}
