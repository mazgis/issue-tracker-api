package lt.mazgis.issuetracker.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.domain.Issue;
import lt.mazgis.issuetracker.domain.Story;
import lt.mazgis.issuetracker.repositories.IssueRepository;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {
  private static final long ANY_ISSUE_ID = 12345l;

  @Mock private IssueRepository issueRepository;

  private IssueService issueService;

  @BeforeEach
  void setUp() {
    this.issueService = new IssueService(this.issueRepository);
  }

  @Test
  void shouldCallRepositoryFindAllAndReturnResultsWhenListAllIssuesCalled() throws Exception {
    final List<Issue> mockedResult = List.of();
    when(this.issueRepository.findAll()).thenReturn(mockedResult);

    final List<Issue> result = this.issueService.listAllIssues();

    verify(this.issueRepository).findAll();

    assertThat(result).isSameAs(mockedResult);
  }

  @Test
  void shouldCallRepositoryFindByIdWithCorrectValuesAndReturnResultsWhenGetIssueByIdCalled()
      throws Exception {
    final Issue mockedIssue = mock(Issue.class);

    when(this.issueRepository.findById(eq(ANY_ISSUE_ID))).thenReturn(Optional.of(mockedIssue));

    final Optional<Issue> result = this.issueService.getIssueById(ANY_ISSUE_ID);

    verify(this.issueRepository).findById(ANY_ISSUE_ID);

    assertThat(result).contains(mockedIssue);
  }

  @Test
  void shouldReturnEmptyIfIssueByIdNotFoundWhenGetIssueByIdCalled() throws Exception {

    when(this.issueRepository.findById(eq(ANY_ISSUE_ID))).thenReturn(Optional.empty());

    final Optional<Issue> result = this.issueService.getIssueById(ANY_ISSUE_ID);

    verify(this.issueRepository).findById(ANY_ISSUE_ID);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldCallRepositorySaveAndFlushWithCorrectValuesAndReturnResultsWhenAddIssueCalled()
      throws Exception {
    final Story mockedResult = mock(Story.class);
    final Story mockedRequest = mock(Story.class);

    when(this.issueRepository.saveAndFlush(any())).thenReturn(mockedResult);

    final Issue result = this.issueService.saveOrUpdateIssue(mockedRequest);

    verify(this.issueRepository).saveAndFlush(mockedRequest);

    assertThat(result).isSameAs(mockedResult);
  }

  @Test
  void shouldCallReporisoryDeleteByidWithCorrectValuesWhenDeleteIssueCalled() throws Exception {

    this.issueService.deleteIssue(ANY_ISSUE_ID);

    verify(this.issueRepository).deleteById(ANY_ISSUE_ID);
  }

  @Test
  void shouldDoCallsInOrderWhenAssignDeveloperToStoryCalled() {
    final Story mockedStory = mock(Story.class);
    final Story mockedSaveResult = mock(Story.class);
    final Developer mockedDeveloper = mock(Developer.class);

    when(this.issueRepository.findById(eq(ANY_ISSUE_ID))).thenReturn(Optional.of(mockedStory));
    when(this.issueRepository.saveAndFlush(eq(mockedStory))).thenReturn(mockedSaveResult);

    final InOrder inOrder = inOrder(this.issueRepository, mockedStory);

    final Optional<Issue> result =
        this.issueService.assignDeveloperToStory(ANY_ISSUE_ID, mockedDeveloper);

    inOrder.verify(this.issueRepository).findById(ANY_ISSUE_ID);
    inOrder.verify(mockedStory).setDeveloper(mockedDeveloper);
    inOrder.verify(this.issueRepository).saveAndFlush(mockedStory);

    assertThat(result).contains(mockedSaveResult);
  }

  @Test
  void shouldReturnOptionalEmptyWhenNoIssueByIdFoundWhenAssignDeveloperToStoryCalled() {
    final Story mockedStory = mock(Story.class);
    final Developer mockedDeveloper = mock(Developer.class);

    when(this.issueRepository.findById(eq(ANY_ISSUE_ID))).thenReturn(Optional.empty());

    final Optional<Issue> result =
        this.issueService.assignDeveloperToStory(ANY_ISSUE_ID, mockedDeveloper);

    verify(this.issueRepository).findById(ANY_ISSUE_ID);
    verify(mockedStory, never()).setDeveloper(mockedDeveloper);
    verify(this.issueRepository, never()).saveAndFlush(mockedStory);

    assertThat(result).isEmpty();
  }
}
