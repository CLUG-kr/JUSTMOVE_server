package justmove.web;

import justmove.config.auth.LoginUser;
import justmove.domain.user.User;
import justmove.service.ActionService;
import justmove.service.exception.ChallengeNotFoundException;
import justmove.web.dto.ApiResponseMessage;
import justmove.web.dto.RegisterActionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/action")
public class ActionController {

    private final ActionService actionService;

    @PostMapping("/challenge/{challengeId}")
    public ResponseEntity<ApiResponseMessage> registerAction(@RequestBody RegisterActionDto dto, @PathVariable(
            "challengeId") Long challengeId, @LoginUser User user) {
        try {
            System.out.println(user);
            System.out.println(challengeId);
            System.out.println(dto);
            actionService.registerAction(user, challengeId, dto);
        } catch (ChallengeNotFoundException e) {
            return new ResponseEntity<>(ApiResponseMessage.fail(HttpStatus.NOT_FOUND.value(), "챌린지가 존재하지 않습니다"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponseMessage.success(HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }
}
