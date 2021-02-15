package justmove.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterActionDto {
    private final Double score;

    @Builder
    public RegisterActionDto(Double score) {
        this.score = score;
    }
}
