package lt.mazgis.issuetracker.api;

import java.util.List;
import java.util.Objects;
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
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.services.DeveloperService;

@Path("/developer")
@Component
public class DeveloperRestApiService {

  private final DeveloperService developerService;

  public DeveloperRestApiService(final DeveloperService developerService) {
    this.developerService = Objects.requireNonNull(developerService);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Developer> listDeveloper() {
    return this.developerService.listAllDevelopers();
  }

  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Developer addDeveloper(final String name) {
    return this.developerService.add(new Developer(name));
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Developer updateDeveloper(@PathParam("id") final long id, final String name)
      throws NotFoundException {
    return this.developerService
        .update(id, name)
        .orElseThrow(() -> new NotFoundException("Developer not found"));
  }

  @DELETE
  @Path("/{id}")
  public void deleteDeveloper(@PathParam("id") final long id) {
    this.developerService.delete(id);
  }
}
