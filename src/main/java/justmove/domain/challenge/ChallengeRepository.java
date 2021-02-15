package justmove.domain.challenge;

import justmove.domain.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByUploader(User uploader);

    List<Challenge> getChallengesByUploaderIn(List<User> uploaders, Sort sort);

    @Query("select c from Challenge c left join c.actions ac group by c.id order by count(ac) desc")
    List<Challenge> getPopularChallenges();
}
