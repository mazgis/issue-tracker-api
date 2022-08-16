package lt.mazgis.issuetracker.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lt.mazgis.issuetracker.domain.Sprint;
import lt.mazgis.issuetracker.domain.Story;
import lt.mazgis.issuetracker.domain.StoryStatus;
import lt.mazgis.issuetracker.repositories.IssueRepository;

@Component
public class SprintPlanningService {

  private static final Comparator<Story> REVERSE_ORDER_BY_STORY_POINTS =
      Collections.reverseOrder(
          (story1, story2) -> Integer.compare(story1.getStoryPoints(), story2.getStoryPoints()));

  private final IssueRepository issueRepository;

  private final int maxStoryPointsPerDeveloperForSprint;

  public SprintPlanningService(
      final IssueRepository issueRepository,
      @Value("${sprint.srory.points.per.developer.max:10}")
          final int maxStoryPointsPerDeveloperForSprint) {
    this.issueRepository = Objects.requireNonNull(issueRepository);
    this.maxStoryPointsPerDeveloperForSprint = maxStoryPointsPerDeveloperForSprint;
  }

  public List<Sprint> plan(final int numberOfDevelopers) {
    final List<Story> stories =
        this.issueRepository.findStoriesByStatus(StoryStatus.ESTIMATED.name());

    Collections.sort(stories, REVERSE_ORDER_BY_STORY_POINTS);

    final int numberOfStoryPointsPerSprint =
        numberOfDevelopers * this.maxStoryPointsPerDeveloperForSprint;

    final List<Sprint> sprints = new ArrayList<>();

    int sprintIndex = 0;

    while (!stories.isEmpty()) {
      final Sprint sprint = planSprint(numberOfStoryPointsPerSprint, stories, sprintIndex);
      stories.removeAll(sprint.getStories());
      sprints.add(sprint);
      sprintIndex++;
    }

    return sprints;
  }

  private Sprint planSprint(
      final int numberOfStoryPointsPerSprint, final List<Story> stories, final int sprintIndex) {
    final Sprint sprint = new Sprint(sprintIndex);

    for (final Story story : stories) {
      final int storyPoints = sprint.getTotalStoryPoints() + story.getStoryPoints();

      if (storyPoints <= numberOfStoryPointsPerSprint) {
        sprint.addStory(story);
      }
    }

    return sprint;
  }
}
