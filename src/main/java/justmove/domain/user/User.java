package justmove.domain.user;

import justmove.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
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

    @ManyToMany()
    @JoinTable(name = "user_follow", joinColumns = @JoinColumn(name = "from_user"), inverseJoinColumns =
    @JoinColumn(name = "to_user"))
    private final Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private final Set<User> followings = new HashSet<>();

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
