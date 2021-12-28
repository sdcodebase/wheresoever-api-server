package wheresoever.quickprotoserver.domain.randommessage.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wheresoever.quickprotoserver.domain.randommessage.application.RandomMessageService;
import wheresoever.quickprotoserver.domain.randommessage.dto.request.SendRmRequest;
import wheresoever.quickprotoserver.domain.randommessage.dto.response.ReceivedMessageResponse;
import wheresoever.quickprotoserver.domain.randommessage.dto.response.SentMessageResponse;
import wheresoever.quickprotoserver.global.argumeentresolver.Session;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/rm")
@Slf4j
@RestController
public class RandomMessageController {

    private final RandomMessageService randomMessageService;

    @PostMapping
    public ResponseEntity<Object> sendMessage(@Session Long memberId, @RequestBody SendRmRequest request) {
        Long messageId = randomMessageService.send(Long.parseLong(request.getReceiverId()), memberId, request.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "messageId", messageId.toString()
                )
        );
    }

    @GetMapping("/received")
    public ResponseEntity<Object> getReceivedRM(@Session Long memberId) {
        List<ReceivedMessageResponse> messages = randomMessageService.getReceivedMessages(memberId)
                .stream()
                .map(ReceivedMessageResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "messages", messages
        ));
    }

    @GetMapping("/sent")
    public ResponseEntity<Object> getSentRM(@Session Long memberId) {
        List<SentMessageResponse> messages = randomMessageService.getSentMessages(memberId)
                .stream()
                .map(SentMessageResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "messages", messages
        ));
    }

    @PostMapping("/grade")
    public ResponseEntity gradeToSender(@Session Long receiverId, @RequestBody Map<String, String> json) {
        Long messageId = Long.getLong(json.get("messageId"));
        Integer grade = Integer.parseInt(json.get("grade"));

        randomMessageService.evaluateMember(receiverId, messageId, grade);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
