package justmove.domain.challenge;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Movie {

    @Column(length = 500, nullable = false)
    private String movieLink;

    @Builder
    public Movie(String movieLink) {
        this.movieLink = movieLink;
    }
}
