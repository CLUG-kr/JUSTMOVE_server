package justmove.domain.user;

import justmove.domain.BaseEntity;
import justmove.domain.challenge.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_follow", joinColumns = @JoinColumn(name = "from_user"), inverseJoinColumns =
    @JoinColumn(name = "to_user"))
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    private Set<User> followings = new HashSet<>();

    @OneToMany(mappedBy = "uploader")
    private List<Challenge> challenges = new ArrayList<>();

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void follow(User target) {
        this.followings.add(target);
        target.followers.add(this);
    }

    public void unfollow(User target) {
        this.followings.remove(target);
        target.followers.remove(this);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
