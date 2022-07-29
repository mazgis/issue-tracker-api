package lt.mazgis.issuetracker.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import lt.mazgis.issuetracker.repositories.DeveloperRepository;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

  private static final String ANY_NEW_NAME = "AnyName";
  private static final long ANY_DEVELOPER_ID = 1L;
  @Mock private DeveloperRepository repository;
  @Mock private Developer developer;

  private DeveloperService developerService;

  @BeforeEach
  public void setUp() {
    this.developerService = new DeveloperService(this.repository);
  }

  @Test
  void souldCallRepositoryFindAllMethodAndForwardReturnedValue() throws Exception {
    final List<Developer> repositoryResponse = List.of();
    when(this.repository.findAll()).thenReturn(repositoryResponse);

    final List<Developer> result = this.developerService.listAllDevelopers();

    verify(this.repository).findAll();

    assertThat(result).isSameAs(repositoryResponse);
  }

  @Test
  void souldCallRepositorySaveAndFlushMethodAndForwardReturnedValue() throws Exception {
    final Developer repositoryResponse = mock(Developer.class);
    when(this.repository.saveAndFlush(any(Developer.class))).thenReturn(repositoryResponse);

    final Developer result = this.developerService.add(this.developer);

    verify(this.repository).saveAndFlush(this.developer);

    assertThat(result).isSameAs(repositoryResponse);
  }

  @Test
  void souldDoCallMethodsInOrderToUpdatedDeveloperNamedAndForwardReturnedValue() throws Exception {
    final Developer repositoryFindResponse = mock(Developer.class);
    final Developer repositorySaveResponse = mock(Developer.class);

    when(this.repository.findById(anyLong())).thenReturn(Optional.of(repositoryFindResponse));
    when(this.repository.saveAndFlush(any(Developer.class))).thenReturn(repositorySaveResponse);

    final InOrder inOrder = inOrder(this.repository, repositoryFindResponse);

    final Optional<Developer> result = this.developerService.update(ANY_DEVELOPER_ID, ANY_NEW_NAME);

    inOrder.verify(this.repository).findById(ANY_DEVELOPER_ID);
    inOrder.verify(repositoryFindResponse).setName(ANY_NEW_NAME);
    inOrder.verify(this.repository).saveAndFlush(repositoryFindResponse);

    assertThat(result).containsSame(repositorySaveResponse);
  }

  @Test
  void souldReturnedEmptyIfDeveloperByIdNotFoundWhenUpdatingName() throws Exception {
    when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

    final Optional<Developer> result = this.developerService.update(ANY_DEVELOPER_ID, ANY_NEW_NAME);

    verify(this.repository).findById(ANY_DEVELOPER_ID);
    verify(this.repository, never()).saveAllAndFlush(any());

    assertThat(result).isEmpty();
  }

  @Test
  void shouldCallReposiotryDeleteByIdWithCorrectParameters() throws Exception {

    this.developerService.delete(ANY_DEVELOPER_ID);

    verify(this.repository).deleteById(ANY_DEVELOPER_ID);
  }

  @Test
  void shouldCallReposiotryFindByIdWithCorrectParametersAndForwardResponse() throws Exception {
    final Developer repositoryFindResponse = mock(Developer.class);
    when(this.repository.findById(anyLong())).thenReturn(Optional.of(repositoryFindResponse));

    final Optional<Developer> result = this.developerService.getDeveloper(ANY_DEVELOPER_ID);

    verify(this.repository).findById(ANY_DEVELOPER_ID);

    assertThat(result).containsSame(repositoryFindResponse);
  }
}
