package lt.mazgis.issuetracker.api;

import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;
import lt.mazgis.issuetracker.domain.Sprint;
import lt.mazgis.issuetracker.services.SprintPlanningService;

@Path("/plan")
@Component
public class SprintPlanningRestApiService {

  private final SprintPlanningService planningService;

  public SprintPlanningRestApiService(final SprintPlanningService planningService) {
    this.planningService = planningService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Sprint> plan(@QueryParam("numberOfDevelopers") final int numberOfDevelopers) {
    if (numberOfDevelopers <= 0) {
      throw new BadRequestException("Nunber of developers should be more when 0");
    }
    return this.planningService.plan(numberOfDevelopers);
  }
}
