package justmove.domain.challenge;

import justmove.domain.BaseEntity;
import justmove.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

}
