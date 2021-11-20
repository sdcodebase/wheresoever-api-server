package wheresoever.quickprotoserver.repository.randommessage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import wheresoever.quickprotoserver.domain.QRandomMessage;
import wheresoever.quickprotoserver.domain.RandomMessage;

import javax.persistence.EntityManager;
import java.util.List;

public class RandomMessageRepositoryCustomImpl implements RandomMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QRandomMessage randomMessage = QRandomMessage.randomMessage;

    public RandomMessageRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<RandomMessage> getSentMessages(Long memberId) {
        return queryFactory
                .selectFrom(randomMessage)
                .where(randomMessage.sender.id.eq(memberId), randomMessage.canceledAt.isNull())
                .join(randomMessage.receiver).fetchJoin()
                .orderBy(randomMessage.at.desc())
                .fetch();
    }

    @Override
    public List<RandomMessage> getReceivedMessages(Long memberId) {
        return queryFactory
                .selectFrom(randomMessage)
                .where(randomMessage.receiver.id.eq(memberId), randomMessage.canceledAt.isNull())
                .join(randomMessage.sender).fetchJoin()
                .orderBy(randomMessage.at.desc())
                .fetch();
    }

    @Override
    public RandomMessage getMessageByReceiverIdAndSenderId(Long receiverId, Long senderId) {
        return queryFactory
                .selectFrom(randomMessage)
                .where(
                        randomMessage.receiver.id.eq(receiverId)
                        , randomMessage.sender.id.eq(senderId)
                        , randomMessage.canceledAt.isNull()
                )
                .fetchOne();
    }
}
