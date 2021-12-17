package wheresoever.quickprotoserver.domain.randommessage.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wheresoever.quickprotoserver.domain.member.dao.MemberRepository;
import wheresoever.quickprotoserver.domain.member.domain.Member;
import wheresoever.quickprotoserver.domain.randommessage.prevSendMessageExistException;
import wheresoever.quickprotoserver.domain.randommessage.dao.RandomMessageRepository;
import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RandomMessageServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RandomMessageRepository randomMessageRepository;

    @InjectMocks
    private RandomMessageService randomMessageService;

    @Test
    void 메시지_보내기() throws Exception {
        //given
        Member receiver = Member.builder()
                .id(1L)
                .email("sdcodebase@gmail.com")
                .build();

        Member sender = Member.builder()
                .id(2L)
                .email("sdkim@gmail.com")
                .build();

        String content = "안녕하세요 반가워요 팔로우 해주세요";

        given(randomMessageRepository.getMessageByReceiverIdAndSenderId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

        given(memberRepository.findById(sender.getId())).willReturn(Optional.of(sender));
        given(memberRepository.findById(receiver.getId())).willReturn(Optional.of(receiver));

        RandomMessage message = RandomMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();

        given(randomMessageRepository.save(any())).willReturn(message);

        //when
        randomMessageService.send(receiver.getId(), sender.getId(), content);

        //then
        assertThat(message.getSender()).isEqualTo(sender);
        assertThat(message.getReceiver()).isEqualTo(receiver);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test
    void 메시지_이미_보낸경우() throws Exception {
        //given
        Member receiver = Member.builder()
                .id(1L)
                .email("sdcodebase@gmail.com")
                .build();

        Member sender = Member.builder()
                .id(2L)
                .email("sdkim@gmail.com")
                .build();

        String content = "안녕하세요 반가워요 팔로우 해주세요";
        RandomMessage message = RandomMessage.builder()
                .id(3L)
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();

        given(randomMessageRepository.getMessageByReceiverIdAndSenderId(anyLong(), anyLong()))
                .willReturn(Optional.of(message));

        //then
        assertThatThrownBy(() -> {
            //when
            randomMessageService.send(receiver.getId(), sender.getId(), content);
        }).isInstanceOf(prevSendMessageExistException.class);
    }

    @Test
    void 메시지_멤버_평가() throws Exception {
        //given
        Member receiver = Member.builder()
                .id(1L)
                .email("sdcodebase@gmail.com")
                .build();

        Member sender = Member.builder()
                .id(2L)
                .email("sdkim@gmail.com")
                .build();

        String content = "안녕하세요 반가워요 팔로우 해주세요";

        int grade = 5;

        RandomMessage message = RandomMessage.builder()
                .id(3L)
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();

        given(randomMessageRepository.findById(anyLong())).willReturn(Optional.of(message));

        //when
        randomMessageService.evaluateMember(receiver.getId(), message.getId(), grade);

        //then
        assertThat(message.getGrade()).isEqualTo(grade);
    }
}