package justmove.domain.challenge;

import justmove.domain.BaseEntity;
import justmove.domain.action.Action;
import justmove.domain.tag.Tag;
import justmove.domain.user.User;
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

    @ManyToOne
    @JoinColumn
    private User uploader;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private final List<Action> actions = new ArrayList<>();

    @ManyToMany(mappedBy = "challenges", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Challenge(String title, String description, Movie movie, User uploader, List<Tag> tags) {
        this.title = title;
        this.description = description;
        this.movie = movie;
        setUploader(uploader);
        this.tags = tags;
    }

    public Challenge setUploader(User uploader) {
        if (this.uploader != null) {
            this.uploader.getChallenges().remove(this);
        }
        this.uploader = uploader;
        uploader.getChallenges().add(this);
        return this;
    }
}
