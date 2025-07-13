package jejaChurch.jeja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jejaChurch.jeja.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamNumber(Integer teamNumber);

    boolean existsByTeamNumber(Integer TeamNumber);
}
