package lt.mazgis.issuetracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import lt.mazgis.issuetracker.domain.Issue;

public interface StoryRepository extends JpaRepository<Issue, Long> {}
