package lt.mazgis.issuetracker.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import lt.mazgis.issuetracker.domain.Bug;
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.domain.Issue;
import lt.mazgis.issuetracker.domain.Story;
import lt.mazgis.issuetracker.repositories.BugRepository;
import lt.mazgis.issuetracker.repositories.IssueRepository;
import lt.mazgis.issuetracker.repositories.StoryRepository;

@Component
public class IssueService {

  private final StoryRepository storyRepository;
  private final BugRepository bugRepository;
  private final IssueRepository issueRepository;

  public IssueService(
      final StoryRepository storyRepository,
      final BugRepository bugRepository,
      final IssueRepository issueRepository) {
    this.storyRepository = Objects.requireNonNull(storyRepository);
    this.bugRepository = Objects.requireNonNull(bugRepository);
    this.issueRepository = Objects.requireNonNull(issueRepository);
  }

  public List<Issue> listAllIssues() {
    //    final List<Issue> issues = new ArrayList<>();
    //    issues.addAll(this.storyRepository.findAll());
    //    issues.addAll(this.bugRepository.findAll());
    //    return issues;
    return this.issueRepository.findAll();
  }

  public Story addStory(final Story story) {
    return this.storyRepository.saveAndFlush(story);
  }

  public Bug addBug(final Bug bug) {
    return this.bugRepository.saveAndFlush(bug);
  }

  public Optional<Issue> assignDeveloper(final long issueId, final Developer developer) {
    return this.issueRepository
        .findById(issueId)
        .map(
            i -> {
              i.setDeveloper(developer);
              return i;
            })
        .map(this.issueRepository::saveAndFlush);
  }
}
