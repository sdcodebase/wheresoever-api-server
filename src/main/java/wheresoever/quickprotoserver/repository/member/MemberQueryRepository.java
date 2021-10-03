package wheresoever.quickprotoserver.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Member;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
}
