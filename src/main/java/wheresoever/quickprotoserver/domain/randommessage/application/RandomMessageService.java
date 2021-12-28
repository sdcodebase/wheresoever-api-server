package wheresoever.quickprotoserver.domain.randommessage.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.member.exception.MemberNotFoundException;
import wheresoever.quickprotoserver.domain.randommessage.exception.PrevSentMessageExistException;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.randommessage.dao.RandomMessageRepository;
import wheresoever.quickprotoserver.global.error.exception.InvalidValueException;

import java.util.List;
import java.util.Optional;

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
        Optional<RandomMessage> prevMessage = randomMessageRepository.getMessageByReceiverIdAndSenderId(receiverId, senderId);
        if (prevMessage.isPresent()) {
            throw new PrevSentMessageExistException();
        }

        Optional<Member> optionalSender = memberRepository.findById(senderId);
        Optional<Member> optionalReceiver = memberRepository.findById(receiverId);
        if (optionalSender.isEmpty() || optionalReceiver.isEmpty()) {
            throw new MemberNotFoundException();
        }

        Member sender = optionalSender.get();
        Member receiver = optionalReceiver.get();
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
            throw new InvalidValueException();
        }

        message.grading(grade);
    }
}
