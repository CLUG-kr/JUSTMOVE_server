package justmove.web.dto;

/*
영상 도전 페이지
영상 소개
올린 사람
랭킹
	도전자 이름 / 점수
평균 점수
내 도전 점수
영상 링크
 */

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeInfoDto {
    String challengeTitle;
    String challengeDescription;
    String movieLink;

    List<String> tags;

    Double averageScore;

    List<RankingDto> rankings;

    @Builder
    public ChallengeInfoDto(String challengeTitle, String challengeDescription, List<String> tags,
                            Double averageScore, List<RankingDto> rankings) {
        this.challengeTitle = challengeTitle;
        this.challengeDescription = challengeDescription;
        this.tags = tags;
        this.averageScore = averageScore;
        this.rankings = rankings;
    }
}
