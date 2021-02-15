package justmove.domain.challenge;

import justmove.domain.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByUploader(User uploader);

    List<Challenge> getChallengesByUploaderIn(List<User> uploaders, Sort sort);
}
