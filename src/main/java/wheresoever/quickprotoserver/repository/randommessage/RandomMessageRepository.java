package wheresoever.quickprotoserver.repository.randommessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Member;
import wheresoever.quickprotoserver.domain.RandomMessage;

import java.util.List;

@Repository
public interface RandomMessageRepository extends JpaRepository<RandomMessage, Long>, RandomMessageRepositoryCustom {

    List<RandomMessage> findRandomMessagesByGradeIsNotNullAndSenderIn(List<Member> members);
}
