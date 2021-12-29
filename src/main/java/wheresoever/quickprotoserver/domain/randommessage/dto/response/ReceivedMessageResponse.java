package wheresoever.quickprotoserver.domain.randommessage.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedMessageResponse {
    private String id;
    private String content;
    private HashMap<String, String> sender = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String grade;

    private ReceivedMessageResponse(RandomMessage message) {
        Member senderInfo = message.getSender();

        this.id = message.getId().toString();
        this.content = message.getContent();
        sender.put("id", senderInfo.getId().toString());
        sender.put("name", senderInfo.getNickname());

        Integer grade = message.getGrade();
        if (grade != null) {
            this.grade = grade.toString();
        }
    }

    public static ReceivedMessageResponse from(RandomMessage message) {
        return new ReceivedMessageResponse(message);
    }

}
