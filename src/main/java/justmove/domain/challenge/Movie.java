package justmove.domain.challenge;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Movie {

    @Column(length = 500, nullable = false)
    private String movieLink;

}
