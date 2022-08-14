package lt.mazgis.issuetracker.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import lt.mazgis.issuetracker.domain.Issue;
import lt.mazgis.issuetracker.domain.Story;

public interface IssueRepository extends JpaRepository<Issue, Long> {

  @Query(
      nativeQuery = true,
      value = "select * from issue where issue_type='STORY' and status=:status")
  List<Story> findStoriesByStatus(@Param("status") String status);
}
