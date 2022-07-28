package lt.mazgis.issuetracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import lt.mazgis.issuetracker.domain.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {}
