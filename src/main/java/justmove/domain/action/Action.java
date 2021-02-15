package justmove.domain.action;

import justmove.domain.BaseEntity;
import justmove.domain.challenge.Challenge;
import justmove.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Action extends BaseEntity {

    @Embedded
    private Score score;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Challenge challenge;

    @Builder
    public Action(Score score, User user, Challenge challenge) {
        this.score = score;
        this.setUser(user);
        this.setChallenge(challenge);
    }

    public Action setChallenge(Challenge challenge) {
        if (this.challenge != null) {
            this.challenge.getActions().remove(this);
        }
        this.challenge = challenge;
        challenge.getActions().add(this);
        return this;
    }

    public Action setUser(User user) {
        if (this.user != null) {
            this.user.getActions().remove(this);
        }
        this.user = user;
        user.getActions().add(this);
        return this;
    }
}
