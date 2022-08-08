package lt.mazgis.issuetracker.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.domain.Issue;
import lt.mazgis.issuetracker.repositories.IssueRepository;

@Component
public class IssueService {

  private final IssueRepository issueRepository;

  public IssueService(final IssueRepository issueRepository) {
    this.issueRepository = Objects.requireNonNull(issueRepository);
  }

  public List<Issue> listAllIssues() {
    return this.issueRepository.findAll();
  }

  public Optional<Issue> getIssueById(final long issueId) {
    return this.issueRepository.findById(issueId);
  }

  public Issue addIssue(final Issue issue) {
    return this.issueRepository.saveAndFlush(issue);
  }

  public Optional<Issue> assignDeveloperToStory(final long issueId, final Developer developer) {
    return this.issueRepository
        .findById(issueId)
        .map(issue -> setDeveloper(developer, issue))
        .map(this.issueRepository::saveAndFlush);
  }

  private Issue setDeveloper(final Developer developer, final Issue i) {
    i.setDeveloper(developer);
    return i;
  }

  public void deleteIssue(final long issueId) {
    this.issueRepository.deleteById(issueId);
  }
}
