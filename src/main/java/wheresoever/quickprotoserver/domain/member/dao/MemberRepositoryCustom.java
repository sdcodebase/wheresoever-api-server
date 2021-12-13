package wheresoever.quickprotoserver.domain.member.dao;

import wheresoever.quickprotoserver.domain.member.domain.Member;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> searchMemberByOption(String metro, LocalDate age_from, LocalDate age_to);
}
