package justmove.domain.action;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Score {
    private Double score;

    @Builder
    public Score(Double score) {
        this.score = score;
    }
}
