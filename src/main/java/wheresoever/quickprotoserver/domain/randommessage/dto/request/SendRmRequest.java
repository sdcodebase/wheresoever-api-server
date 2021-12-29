package wheresoever.quickprotoserver.domain.randommessage.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SendRmRequest {
    private String receiverId;
    private String content;
}
