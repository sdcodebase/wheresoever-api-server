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

    private Member generateEmptyMember() {
        return Member.builder().build();
    }

    @Test
    void getSentMessages() {
        Member sender = generateEmptyMember();
        Member receiver1 = generateEmptyMember();
        Member receiver2 = generateEmptyMember();
        Member receiver3 = generateEmptyMember();

        memberRepository.saveAll(List.of(sender, receiver1, receiver2, receiver3));

        RandomMessage message1 = new RandomMessage(sender, receiver1, "hello");
        RandomMessage message2 = new RandomMessage(sender, receiver2, "hello");

        RandomMessage message3 = new RandomMessage(sender, receiver3, "hello");
        message3.delete();

        messageRepository.saveAll(List.of(message1, message2, message3));

        em.flush();
        em.clear();

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
        Member receiver = generateEmptyMember();
        Member sender1 = generateEmptyMember();
        Member sender2 = generateEmptyMember();
        Member sender3 = generateEmptyMember();

        memberRepository.saveAll(List.of(receiver, sender1, sender2, sender3));

        RandomMessage message1 = new RandomMessage(sender1, receiver, "hello");
        RandomMessage message2 = new RandomMessage(sender2, receiver, "hello");

        RandomMessage message3 = new RandomMessage(sender3, receiver, "hello");
        message3.delete();

        messageRepository.saveAll(List.of(message1, message2, message3));

        em.flush();
        em.clear();

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