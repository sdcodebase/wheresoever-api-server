package wheresoever.quickprotoserver.domain.randommessage.dao;

import wheresoever.quickprotoserver.domain.randommessage.domain.RandomMessage;

import java.util.List;
import java.util.Optional;

public interface RandomMessageRepositoryCustom {

    List<RandomMessage> getSentMessages(Long memberId);

    List<RandomMessage> getReceivedMessages(Long memberId);

    Optional<RandomMessage> getMessageByReceiverIdAndSenderId(Long receiverId, Long senderId);
}
