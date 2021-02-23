package justmove.domain.challenge;

import justmove.domain.BaseEntity;
import justmove.domain.action.Action;
import justmove.domain.tag.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Challenge extends BaseEntity {

    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "text", nullable = false)
    private String description;

    @Embedded
    private Movie movie;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private final List<Action> actions = new ArrayList<>();

    @ManyToMany(mappedBy = "challenges", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Challenge(String title, String description, Movie movie, List<Tag> tags) {
        this.title = title;
        this.description = description;
        this.movie = movie;
        this.tags = tags;
    }
}
