package justmove.web;

import justmove.config.auth.LoginSessionUser;
import justmove.config.auth.dto.SessionUser;
import justmove.service.SocialService;
import justmove.service.exception.UserNotFoundException;
import justmove.web.dto.ApiResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/social")
public class SocialController {

    private final SocialService socialService;

    @PostMapping("/follow/{targetId}")
    public ResponseEntity<ApiResponseMessage> follow(@LoginSessionUser() SessionUser fromUser,
                                                     @PathVariable("targetId") Long targetId) {
        try {
            socialService.follow(fromUser.getId(), targetId);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(ApiResponseMessage.fail(404001, "Follow할 수 없는 사용자입니다"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponseMessage.success(HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @PostMapping("/unfollow/{targetId}")
    public ResponseEntity<ApiResponseMessage> unfollow(@LoginSessionUser() SessionUser fromUser,
                                                       @PathVariable("targetId") Long targetId) {
        try {
            socialService.unfollow(fromUser.getId(), targetId);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(ApiResponseMessage.fail(404001, "Unfollow할 수 없는 사용자입니다"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ApiResponseMessage.success(HttpStatus.ACCEPTED.value()), HttpStatus.ACCEPTED);
    }

//    @GetMapping("/following/challenge")
//    public ResponseEntity<ApiResponseMessage> getFollowingChallenges(@LoginUser User user) {
//        return new ResponseEntity<>(ApiResponseMessage.data(HttpStatus.OK.value(),
//                socialService.getFollowingChallenges(user)), HttpStatus.OK);
//    }
}
