package justmove.domain.tag;

import justmove.domain.BaseEntity;
import justmove.domain.challenge.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Tag extends BaseEntity {

    @Column(nullable = false)
    private String name;


    @ManyToMany
    @JoinTable(name = "challenge_tag", joinColumns = @JoinColumn(name = "tag"), inverseJoinColumns =
    @JoinColumn(name = "challenge"))
    private List<Challenge> challenges = new ArrayList<>();

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
