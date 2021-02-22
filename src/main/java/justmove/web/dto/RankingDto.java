package justmove.web.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankingDto {
    private String userName;
    private Double score;

    @Builder
    public RankingDto(String userName, Double score) {
        this.userName = userName;
        this.score = score;
    }
}
