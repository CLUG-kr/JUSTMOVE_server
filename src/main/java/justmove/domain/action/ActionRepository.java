package justmove.domain.action;

import justmove.domain.challenge.Challenge;
import justmove.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByUser(User user);

    Optional<Action> findByUserAndChallenge(User user, Challenge challenge);

    List<Action> findByChallenge(Challenge challenge);
}
