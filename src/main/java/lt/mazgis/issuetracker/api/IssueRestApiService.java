package lt.mazgis.issuetracker.api;

import java.util.List;
import java.util.Objects;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;
import lt.mazgis.issuetracker.domain.Bug;
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.domain.Issue;
import lt.mazgis.issuetracker.domain.Story;
import lt.mazgis.issuetracker.services.DeveloperService;
import lt.mazgis.issuetracker.services.IssueService;

@Path("/issue")
@Component
public class IssueRestApiService {

  private final IssueService issueService;
  private final DeveloperService developerService;

  public IssueRestApiService(
      final IssueService issueService, final DeveloperService developerService) {
    this.issueService = Objects.requireNonNull(issueService);
    this.developerService = Objects.requireNonNull(developerService);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Issue> listAllIssues() {
    return this.issueService.listAllIssues();
  }

  @POST
  @Path("/story")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Story addStory(final Story story) {
    return this.issueService.addStory(story);
  }

  @POST
  @Path("/bug")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Bug addBug(final Bug bug) {
    return this.issueService.addBug(bug);
  }

  public Issue assingDeveloper(final long issueId, final long developerId) {
    final Developer developer =
        this.developerService.getDeveloper(developerId).orElseThrow(NotFoundException::new);
    return this.issueService
        .assignDeveloper(issueId, developer)
        .orElseThrow(NotFoundException::new);
  }
}
