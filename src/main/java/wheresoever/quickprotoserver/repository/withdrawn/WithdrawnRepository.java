package wheresoever.quickprotoserver.repository.withdrawn;

import org.springframework.data.jpa.repository.JpaRepository;
import wheresoever.quickprotoserver.domain.Withdrawn;

public interface WithdrawnRepository extends JpaRepository<Withdrawn, Long> {
}
