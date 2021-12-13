package wheresoever.quickprotoserver.domain.member.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.member.domain.Member;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
}
