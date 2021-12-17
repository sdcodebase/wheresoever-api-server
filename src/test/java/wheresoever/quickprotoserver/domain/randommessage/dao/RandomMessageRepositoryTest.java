package wheresoever.quickprotoserver.domain.randommessage.dao;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RandomMessageRepositoryTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RandomMessageRepository messageRepository;

    @Test
    void getSentMessages() {
        Member sender = Member.builder()
                .email("sdcodebase@gmail.com")
                .build();

        Member receiver1 = Member.builder()
                .email("r1@gmail.com")
                .build();

        Member receiver2 = Member.builder()
                .email("r2@gmail.com")
                .build();

        Member receiver3 = Member.builder()
                .email("r3@gmail.com")
                .build();

        memberRepository.saveAll(List.of(sender, receiver1, receiver2, receiver3));

        RandomMessage message1 = new RandomMessage(sender, receiver1, "hello");
        RandomMessage message2 = new RandomMessage(sender, receiver2, "hello");

        RandomMessage message3 = new RandomMessage(sender, receiver3, "hello");
        message3.delete();

        messageRepository.saveAll(List.of(message1, message2, message3));

        List<RandomMessage> sentMessages = messageRepository.getSentMessages(sender.getId());

        // 내림차순으로 정렬
        RandomMessage first = sentMessages.get(0);
        RandomMessage second = sentMessages.get(1);
        assertThat(first.getAt()).isAfter(second.getAt());

        // 삭제된 메시지는 리스팅X
        boolean existenceOfMessage3 = sentMessages.stream().anyMatch(message -> message.equals(message3));
        assertThat(existenceOfMessage3).isFalse();

        // receiver fetch join 되었나 확인
        boolean isReceiverFetched = emf.getPersistenceUnitUtil().isLoaded(first.getReceiver());
        assertThat(isReceiverFetched).isEqualTo(true);
    }

    @Test
    void getReceiveMessages() {
        Member receiver = Member.builder()
                .email("sdcodebase@gmail.com")
                .build();

        Member sender1 = Member.builder()
                .email("s1@gmail.com")
                .build();

        Member sender2 = Member.builder()
                .email("s2@gmail.com")
                .build();

        Member sender3 = Member.builder()
                .email("s3@gmail.com")
                .build();

        memberRepository.saveAll(List.of(receiver, sender1, sender2, sender3));

        RandomMessage message1 = new RandomMessage(sender1, receiver, "hello");
        RandomMessage message2 = new RandomMessage(sender2, receiver, "hello");

        RandomMessage message3 = new RandomMessage(sender3, receiver, "hello");
        message3.delete();

        messageRepository.saveAll(List.of(message1, message2, message3));

        List<RandomMessage> receivedMessages = messageRepository.getReceivedMessages(receiver.getId());

        // 내림차순으로 정렬
        RandomMessage first = receivedMessages.get(0);
        RandomMessage second = receivedMessages.get(1);
        assertThat(first.getAt()).isAfter(second.getAt());

        // 삭제된 메시지는 리스팅X
        boolean existenceOfMessage3 = receivedMessages.stream().anyMatch(message -> message.equals(message3));
        assertThat(existenceOfMessage3).isFalse();

        // sender fetch join 되었나 확인
        boolean isReceiverFetched = emf.getPersistenceUnitUtil().isLoaded(first.getSender());
        assertThat(isReceiverFetched).isEqualTo(true);
    }
}