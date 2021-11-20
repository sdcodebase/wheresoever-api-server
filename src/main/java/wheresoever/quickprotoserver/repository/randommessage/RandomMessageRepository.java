package wheresoever.quickprotoserver.repository.randommessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.RandomMessage;

@Repository
public interface RandomMessageRepository extends JpaRepository<RandomMessage, Long>, RandomMessageRepositoryCustom {

}
