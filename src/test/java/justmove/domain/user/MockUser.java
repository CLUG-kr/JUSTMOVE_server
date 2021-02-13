package justmove.domain.user;

public class MockUser {

    public static User mockUser1() {
        return User.builder().name("홍길동").email("test@test.com").picture("http://image.png").role(Role.USER).build();
    }

    public static User mockUser2() {
        return User.builder().name("김동수").email("asdf@test.com").picture("http://picture.png").role(Role.USER).build();
    }

    public static User mockUser3() {
        return User.builder().name("김갑자").email("1234@test.com").picture("http://some.png").role(Role.USER).build();
    }
}
