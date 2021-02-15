package justmove.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterActionDto {
    private Double score;

    @Builder
    public RegisterActionDto(Double score) {
        this.score = score;
    }
}
