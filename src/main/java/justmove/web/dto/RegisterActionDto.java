package justmove.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterActionDto {
    private Double score;

    private String userName;

    @Builder
    public RegisterActionDto(Double score, String userName) {
        this.score = score;
        this.userName = userName;
    }
}
