package justmove.service;

import justmove.domain.action.Action;
import justmove.domain.action.ActionRepository;
import justmove.domain.action.Score;
import justmove.domain.challenge.Challenge;
import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.user.User;
import justmove.service.exception.ChallengeNotFoundException;
import justmove.web.dto.RegisterActionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final ChallengeRepository challengeRepository;

    public Action registerAction(User user, Long challengeId, RegisterActionDto dto) throws ChallengeNotFoundException {
        Challenge challenge =
                challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeNotFoundException(
                        "challengeId -> " + challengeId));

        Score score = Score.builder().score(dto.getScore()).build();
        Action action = Action.builder().user(user).score(score).challenge(challenge).build();

        return actionRepository.save(action);
    }

}
