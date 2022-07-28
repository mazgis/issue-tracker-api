package lt.mazgis.issuetracker.api;

import java.util.Objects;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonConfig implements ContextResolver<ObjectMapper> {

  private final ObjectMapper mapper;

  public JsonConfig(final ObjectMapper mapper) {
    this.mapper = Objects.requireNonNull(mapper);
  }

  @Override
  public ObjectMapper getContext(final Class<?> type) {
    return this.mapper;
  }
}
