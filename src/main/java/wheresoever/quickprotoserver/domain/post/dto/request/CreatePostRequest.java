package wheresoever.quickprotoserver.domain.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreatePostRequest {
    @NotEmpty
    private String content;

    @NotEmpty
    private String category;
}
