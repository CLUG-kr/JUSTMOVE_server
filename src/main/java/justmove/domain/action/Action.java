package justmove.domain.action;

import justmove.domain.BaseEntity;
import justmove.domain.challenge.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Getter
@Entity
public class Action extends BaseEntity {

    @Embedded
    private Score score;

    private String userName;

    @ManyToOne
    @JoinColumn
    private Challenge challenge;

    @Builder
    public Action(Score score, String userName, Challenge challenge) {
        this.score = score;
        this.userName = userName;
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
}
