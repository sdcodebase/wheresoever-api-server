package wheresoever.quickprotoserver.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.randommessage.application.RandomMessageService;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;
import wheresoever.quickprotoserver.domain.model.Sex;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.randommessage.dao.RandomMessageRepository;

import java.time.LocalDate;


@SpringBootTest
@Transactional
class RandomMessageServiceTest {


    @Autowired
    private RandomMessageService randomMessageService;

    @Autowired
    private RandomMessageRepository randomMessageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 메시지_보내기() throws Exception {
        //given
        Member receiver = memberRepository.save(new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울"));
        Member sender = memberRepository.save(new Member("aaa@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));
        String content = "안녕하세요 반가워요 팔로우 해주세요";

        //when

        Long messageId = randomMessageService.send(receiver.getId(), sender.getId(), content);

        //then
        RandomMessage message = randomMessageRepository.findById(messageId).get();

        Assertions.assertThat(message.getSender().getId()).isEqualTo(sender.getId());
        Assertions.assertThat(message.getReceiver().getId()).isEqualTo(receiver.getId());
        Assertions.assertThat(message.getContent()).isEqualTo(content);

    }

    @Test
    public void 메시지_이미_보낸경우() throws Exception {
        //given
        Member receiver = memberRepository.save(new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울"));
        Member sender = memberRepository.save(new Member("aaa@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));
        String content = "안녕하세요 반가워요 팔로우 해주세요";

        Long messageId = randomMessageService.send(receiver.getId(), sender.getId(), content);

        //then
        Assertions.assertThatThrownBy(() -> {
            //when
            randomMessageService.send(receiver.getId(), sender.getId(), content);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 메시지_멤버_평가() throws Exception {
        //given
        Member receiver = memberRepository.save(new Member("sdkim@gmail.com", "1234", Sex.MALE, "sundo", LocalDate.now(), "서울"));
        Member sender = memberRepository.save(new Member("aaa@gmail.com", "1234", Sex.MALE, "sda", LocalDate.now(), "서울"));
        String content = "안녕하세요 반가워요 팔로우 해주세요";

        Long messageId = randomMessageService.send(receiver.getId(), sender.getId(), content);

        //when
        int grade = 5;
        randomMessageService.evaluateMember(receiver.getId(), messageId, grade);

        //then

        RandomMessage message = randomMessageRepository.findById(messageId).get();

        Assertions.assertThat(message.getReceiver().getId()).isEqualTo(receiver.getId());
        Assertions.assertThat(message.getGrade()).isEqualTo(grade);
    }
}