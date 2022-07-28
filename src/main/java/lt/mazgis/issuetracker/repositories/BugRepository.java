package lt.mazgis.issuetracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import lt.mazgis.issuetracker.domain.Bug;

public interface BugRepository extends JpaRepository<Bug, Long> {}
