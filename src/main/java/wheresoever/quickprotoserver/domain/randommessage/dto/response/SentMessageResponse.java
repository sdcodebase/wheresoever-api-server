package wheresoever.quickprotoserver.domain.randommessage.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentMessageResponse {
    private String id;
    private String content;
    private HashMap<String, String> receiver = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String grade;

    private SentMessageResponse(RandomMessage message) {
        Member receiverInfo = message.getReceiver();

        this.id = message.getId().toString();
        this.content = message.getContent();
        receiver.put("id", receiverInfo.getId().toString());
        receiver.put("name", receiverInfo.getNickname());

        Integer grade = message.getGrade();
        if (grade != null) {
            this.grade = grade.toString();
        }
    }

    public static SentMessageResponse from(RandomMessage message) {
        return new SentMessageResponse(message);
    }
}
