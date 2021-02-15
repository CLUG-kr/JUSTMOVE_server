package justmove.web;

import com.amazonaws.SdkClientException;
import justmove.config.auth.LoginUser;
import justmove.domain.user.User;
import justmove.service.ChallengeService;
import justmove.web.dto.ApiResponseMessage;
import justmove.web.dto.RegisterChallengeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping(value = "")
    public ResponseEntity<ApiResponseMessage> registerChallenge(@ModelAttribute RegisterChallengeRequestDto dto,
                                                                @LoginUser User user) {
        try {
            challengeService.registerChallenge(dto, user);
        } catch (SdkClientException e) {
            return new ResponseEntity<>(ApiResponseMessage.fail(HttpStatus.SERVICE_UNAVAILABLE.value(), "서버에 영상을 " +
                    "저장하는데 " +
                    "실패했습니다"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (IOException e) {
            return new ResponseEntity<>(ApiResponseMessage.fail(HttpStatus.SERVICE_UNAVAILABLE.value(), "영상을 올리는데 " +
                    "실패했습니다"), HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<>(ApiResponseMessage.success(HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponseMessage> getPopularChallenges() {
        return new ResponseEntity<>(ApiResponseMessage.data(HttpStatus.OK.value(),
                challengeService.getPopularChallenges()), HttpStatus.OK);
    }

}
