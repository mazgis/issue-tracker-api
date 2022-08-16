package lt.mazgis.issuetracker.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import lt.mazgis.issuetracker.domain.Sprint;
import lt.mazgis.issuetracker.domain.Story;
import lt.mazgis.issuetracker.domain.StoryStatus;
import lt.mazgis.issuetracker.repositories.IssueRepository;

@ExtendWith(MockitoExtension.class)
class SprintPlanningServiceTest {

  private static final int MAX_STORY_POINTS = 10;

  @Mock private IssueRepository issueRepository;

  private SprintPlanningService planningService;

  @BeforeEach
  void setUp() {
    this.planningService = new SprintPlanningService(this.issueRepository, MAX_STORY_POINTS);
  }

  @Test
  void shouldAddIssueWithMaxStoryPointsFirst() {
    final Story mockedStory1 = mock(Story.class);
    final Story mockedStory2 = mock(Story.class);

    when(mockedStory1.getStoryPoints()).thenReturn(1);
    when(mockedStory2.getStoryPoints()).thenReturn(MAX_STORY_POINTS);

    final ArrayList<Story> mockedStoriesList = new ArrayList<>();
    mockedStoriesList.add(mockedStory1);
    mockedStoriesList.add(mockedStory2);

    when(this.issueRepository.findStoriesByStatus(eq(StoryStatus.ESTIMATED.name())))
        .thenReturn(mockedStoriesList);

    final List<Sprint> plan = this.planningService.plan(1);

    assertThat(plan)
        .first()
        .matches(sprint -> sprint.getIndex() == 0)
        .matches(sprint -> sprint.getTotalStoryPoints() == MAX_STORY_POINTS)
        .extracting(Sprint::getStories)
        .asList()
        .containsOnly(mockedStory2);

    assertThat(plan)
        .element(1)
        .matches(sprint -> sprint.getIndex() == 1)
        .matches(sprint -> sprint.getTotalStoryPoints() == 1)
        .extracting(Sprint::getStories)
        .asList()
        .containsOnly(mockedStory1);
  }

  @Test
  void shouldFillSprintWithNextBiggestStory() {
    final Story mockedStory1 = mock(Story.class);
    final Story mockedStory2 = mock(Story.class);
    final Story mockedStory3 = mock(Story.class);

    when(mockedStory1.getStoryPoints()).thenReturn(1);
    when(mockedStory2.getStoryPoints()).thenReturn(MAX_STORY_POINTS - 2);
    when(mockedStory3.getStoryPoints()).thenReturn(2);

    final ArrayList<Story> mockedStoriesList = new ArrayList<>();
    mockedStoriesList.add(mockedStory1);
    mockedStoriesList.add(mockedStory2);
    mockedStoriesList.add(mockedStory3);

    when(this.issueRepository.findStoriesByStatus(eq(StoryStatus.ESTIMATED.name())))
        .thenReturn(mockedStoriesList);

    final List<Sprint> plan = this.planningService.plan(1);

    assertThat(plan)
        .first()
        .matches(sprint -> sprint.getIndex() == 0)
        .matches(sprint -> sprint.getTotalStoryPoints() == MAX_STORY_POINTS)
        .extracting(Sprint::getStories)
        .asList()
        .containsOnly(mockedStory2, mockedStory3);

    assertThat(plan)
        .element(1)
        .matches(sprint -> sprint.getIndex() == 1)
        .matches(sprint -> sprint.getTotalStoryPoints() == 1)
        .extracting(Sprint::getStories)
        .asList()
        .containsOnly(mockedStory1);
  }

  @Test
  void shouldFillSprintWithSkipNextBiggestStoryIfDoesNotFit() {
    final Story mockedStory1 = mock(Story.class);
    final Story mockedStory2 = mock(Story.class);
    final Story mockedStory3 = mock(Story.class);
    final Story mockedStory4 = mock(Story.class);

    when(mockedStory1.getStoryPoints()).thenReturn(1);
    when(mockedStory2.getStoryPoints()).thenReturn(MAX_STORY_POINTS - 3);
    when(mockedStory3.getStoryPoints()).thenReturn(2);
    when(mockedStory4.getStoryPoints()).thenReturn(4);

    final ArrayList<Story> mockedStoriesList = new ArrayList<>();
    mockedStoriesList.add(mockedStory1);
    mockedStoriesList.add(mockedStory2);
    mockedStoriesList.add(mockedStory3);
    mockedStoriesList.add(mockedStory4);

    when(this.issueRepository.findStoriesByStatus(eq(StoryStatus.ESTIMATED.name())))
        .thenReturn(mockedStoriesList);

    final List<Sprint> plan = this.planningService.plan(1);

    assertThat(plan)
        .first()
        .matches(sprint -> sprint.getIndex() == 0)
        .matches(sprint -> sprint.getTotalStoryPoints() == MAX_STORY_POINTS)
        .extracting(Sprint::getStories)
        .asList()
        .containsOnly(mockedStory2, mockedStory3, mockedStory1);

    assertThat(plan)
        .element(1)
        .matches(sprint -> sprint.getIndex() == 1)
        .matches(sprint -> sprint.getTotalStoryPoints() == 4)
        .extracting(Sprint::getStories)
        .asList()
        .containsOnly(mockedStory4);
  }
}
