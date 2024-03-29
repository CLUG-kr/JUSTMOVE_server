package justmove.service;

import justmove.domain.challenge.ChallengeRepository;
import justmove.domain.user.User;
import justmove.domain.user.UserRepository;
import justmove.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SocialService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public void follow(Long fromId, Long targetId) {
        if (fromId.equals(targetId)) return;

        User fromUser = userRepository.findById(fromId).orElseThrow(() -> new UserNotFoundException("id : " + fromId));
        User targetUser =
                userRepository.findById(targetId).orElseThrow(() -> new UserNotFoundException("id : " + targetId));

        fromUser.follow(targetUser);
    }

    @Transactional
    public void unfollow(Long fromId, Long targetId) {
        if (fromId.equals(targetId)) return;

        User fromUser = userRepository.findById(fromId).orElseThrow(() -> new UserNotFoundException("id : " + fromId));
        User targetUser =
                userRepository.findById(targetId).orElseThrow(() -> new UserNotFoundException("id : " + targetId));

        fromUser.unfollow(targetUser);
    }

//    @Transactional(readOnly = true)
//    public List<Challenge> getFollowingChallenges(User user) {
//        List<User> followings = new ArrayList<>(user.getFollowings());
//        return challengeRepository.getChallengesByUploaderIn(followings, Sort.by("createdDate").descending());
//    }
}
