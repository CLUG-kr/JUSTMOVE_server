package justmove.web.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
public class RegisterChallengeRequestDto implements Serializable {

    private String title;
    private String description;
    private MultipartFile movie;
    private List<String> tags;

    @Builder
    public RegisterChallengeRequestDto(String title, String description, MultipartFile movie, List<String> tags) {
        this.title = title;
        this.description = description;
        this.movie = movie;
        this.tags = tags;
    }

}
