package wheresoever.quickprotoserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.RandomMessage;
import wheresoever.quickprotoserver.repository.member.MemberRepository;
import wheresoever.quickprotoserver.repository.randommessage.RandomMessageRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RandomMessageService {

    private final RandomMessageRepository randomMessageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    // todo: receiverId를 랜덤하게 하도록 바꾸기(매칭 알고리즘 고안)
    public Long send(Long receiverId, Long senderId, String content) {
        RandomMessage prevMessage = randomMessageRepository.getMessageByReceiverIdAndSenderId(receiverId, senderId);
        if (Objects.nonNull(prevMessage)) {
            String msg = "이미 메시지를 보낸 기록이 있습니다.";
            log.info(msg);
            throw new IllegalStateException(msg);
        }

        Member sender = memberRepository.findById(senderId).get();
        Member receiver = memberRepository.findById(receiverId).get();

        RandomMessage message = new RandomMessage(sender, receiver, content);

        return randomMessageRepository.save(message).getId();
    }

    public List<RandomMessage> getReceivedMessages(Long memberId) {
        return randomMessageRepository.getReceivedMessages(memberId);
    }

    public List<RandomMessage> getSentMessages(Long memberId) {
        return randomMessageRepository.getSentMessages(memberId);
    }

    @Transactional
    public void evaluateMember(Long memberId, Long messageId, int grade) {
        RandomMessage message = randomMessageRepository.findById(messageId).get();
        if (!message.getReceiver().getId().equals(memberId)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        message.grading(grade);
    }
}
