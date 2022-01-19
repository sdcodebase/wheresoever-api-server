package wheresoever.quickprotoserver.domain.randommessage.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SendRmRequest {
    @NotEmpty
    private String receiverId;

    @NotNull
    private String content;
}
