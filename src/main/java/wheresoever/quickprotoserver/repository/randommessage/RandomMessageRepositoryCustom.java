package wheresoever.quickprotoserver.repository.randommessage;

import wheresoever.quickprotoserver.domain.RandomMessage;

import java.util.List;

public interface RandomMessageRepositoryCustom {

    List<RandomMessage> getSentMessages(Long memberId);

    List<RandomMessage> getReceivedMessages(Long memberId);

    RandomMessage getMessageByReceiverIdAndSenderId(Long receiverId, Long senderId);
}
