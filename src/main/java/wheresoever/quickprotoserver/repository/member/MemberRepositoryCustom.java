package wheresoever.quickprotoserver.repository.member;

import wheresoever.quickprotoserver.domain.Member;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> searchMemberByOption(String metro, LocalDate age_from, LocalDate age_to);
}
