package wheresoever.quickprotoserver.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.QMember;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QMember member = QMember.member;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Member> searchMemberByOption(String metro, LocalDate age_from, LocalDate age_to) {
        return queryFactory
                .selectFrom(member)
                .where(
                        member.metropolitan.eq(metro)
//                        , member.birthdate.goe(age_from)
//                        , member.birthdate.loe(age_to)
                )
                .fetch();
    }
}
