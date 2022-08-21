package lt.mazgis.issuetracker.api;

import java.util.List;
import java.util.Objects;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;
import lt.mazgis.issuetracker.domain.Bug;
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.domain.Issue;
import lt.mazgis.issuetracker.domain.IssueType;
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

  @GET
  @Path("/story/{storyId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Story getStory(@PathParam("storyId") final long stroyId) {
    return this.issueService
        .getIssueById(stroyId)
        .filter(issue -> IssueType.STORY == issue.getIssueType())
        .map(Story.class::cast)
        .orElseThrow(() -> new NotFoundException("Story by given id not found"));
  }

  @POST
  @Path("/story")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Story addStory(final Story story) {
    validateAddRequestHasNoId(story);
    return this.issueService.saveOrUpdateIssue(story);
  }

  @PUT
  @Path("/story/{storyId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Story updateStory(@PathParam("storyId") final long stroyId, final Story newStoryValues) {
    validateAddRequestHasNoId(newStoryValues);
    return this.issueService
        .getIssueById(stroyId)
        .filter(issue -> IssueType.STORY == issue.getIssueType())
        .map(Story.class::cast)
        .map(story -> story.merge(newStoryValues))
        .map(this.issueService::saveOrUpdateIssue)
        .orElseThrow(() -> new NotFoundException("Story by given id not found"));
  }

  @GET
  @Path("/bug/{bugId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Bug getBug(@PathParam("bugId") final long bugId) {
    return this.issueService
        .getIssueById(bugId)
        .filter(issue -> IssueType.BUG == issue.getIssueType())
        .map(Bug.class::cast)
        .orElseThrow(() -> new NotFoundException("Bug by given id not found"));
  }

  @POST
  @Path("/bug")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Bug addBug(final Bug bug) {
    validateAddRequestHasNoId(bug);
    return this.issueService.saveOrUpdateIssue(bug);
  }

  @PUT
  @Path("/bug/{bugId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Bug updateBug(@PathParam("bugId") final long bugId, final Bug newBugValues) {
    validateAddRequestHasNoId(newBugValues);
    return this.issueService
        .getIssueById(bugId)
        .filter(issue -> IssueType.BUG == issue.getIssueType())
        .map(Bug.class::cast)
        .map(bug -> bug.merge(newBugValues))
        .map(this.issueService::saveOrUpdateIssue)
        .orElseThrow(() -> new NotFoundException("Bug by given id not found"));
  }

  @DELETE
  @Path("/{issueId}")
  public void deleteIssue(@PathParam("issueId") final long issueId) {
    this.issueService.deleteIssue(issueId);
  }

  @PUT
  @Path("{issueId}/{developerId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Issue assingDeveloper(
      @PathParam("issueId") final long issueId, @PathParam("developerId") final long developerId) {
    final Developer developer =
        this.developerService
            .getDeveloper(developerId)
            .orElseThrow(() -> new NotFoundException("Developer not found"));
    return this.issueService
        .assignDeveloperToStory(issueId, developer)
        .orElseThrow(() -> new NotFoundException("Issue not found"));
  }

  private void validateAddRequestHasNoId(final Issue issue) {
    if (issue.getIssueId() != null) {
      throw new BadRequestException(
          "Request should not contains issueId. To update exising issue user other method.");
    }
  }
}
