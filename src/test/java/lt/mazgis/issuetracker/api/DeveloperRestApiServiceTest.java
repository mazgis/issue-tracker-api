package lt.mazgis.issuetracker.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.services.DeveloperService;

@ExtendWith(MockitoExtension.class)
class DeveloperRestApiServiceTest {

  private static final String ANY_NAME = "any_name";

  private static final long ANY_DEVELOPER_ID = 1234l;

  private final ObjectMapper mapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          .setSerializationInclusion(Include.NON_NULL);

  @Mock private DeveloperService developerService;

  private DeveloperRestApiService apiService;

  private Dispatcher dispatcher;

  private @BeforeEach void setUp() {
    this.apiService = new DeveloperRestApiService(this.developerService);
    this.dispatcher = MockDispatcherFactory.createDispatcher();
    this.dispatcher.getRegistry().addSingletonResource(this.apiService);
  }

  @Test
  void shouldReturnListOfDevelopers() throws Exception {
    final MockHttpRequest mockHttpRequest = MockHttpRequest.get("/developer");
    final MockHttpResponse mockHttpResponse = new MockHttpResponse();

    final List<Developer> mockedResponse = List.of(new Developer(ANY_NAME));

    when(this.developerService.listAllDevelopers()).thenReturn(mockedResponse);

    this.dispatcher.invoke(mockHttpRequest, mockHttpResponse);

    verify(this.developerService).listAllDevelopers();

    assertThat((MediaType) mockHttpResponse.getOutputHeaders().getFirst("Content-Type"))
        .isEqualTo(MediaType.APPLICATION_JSON_TYPE);

    final List<Developer> responseData =
        this.mapper.readValue(
            mockHttpResponse.getContentAsString(), new TypeReference<List<Developer>>() {});

    assertThat(responseData).singleElement();
    assertThat(responseData).allMatch(d -> d.getName().equals(ANY_NAME));
  }

  @Test
  void shouldCallAddDeveloperAndReturnValue() throws Exception {
    final MockHttpRequest mockHttpRequest =
        MockHttpRequest.post("/developer/")
            .contentType(MediaType.TEXT_PLAIN)
            .content(ANY_NAME.getBytes());
    final MockHttpResponse mockHttpResponse = new MockHttpResponse();

    final Developer mockedResponse = new Developer(ANY_NAME);

    when(this.developerService.add(ArgumentMatchers.any(Developer.class)))
        .thenReturn(mockedResponse);

    this.dispatcher.invoke(mockHttpRequest, mockHttpResponse);

    verify(this.developerService).add(argThat(d -> d.getName().equals(ANY_NAME)));

    final Developer responseData =
        this.mapper.readValue(mockHttpResponse.getContentAsString(), Developer.class);

    assertThat(responseData).matches(d -> d.getName().equals(ANY_NAME));
  }

  @Test
  void shouldCallUpdateDeveloperNameByIdAndReturnValue() throws Exception {
    final MockHttpRequest mockHttpRequest =
        MockHttpRequest.put("/developer/" + ANY_DEVELOPER_ID)
            .contentType(MediaType.TEXT_PLAIN)
            .content(ANY_NAME.getBytes());
    final MockHttpResponse mockHttpResponse = new MockHttpResponse();

    final Developer mockedResponse = new Developer(ANY_NAME);

    when(this.developerService.update(ANY_DEVELOPER_ID, ANY_NAME))
        .thenReturn(Optional.of(mockedResponse));

    this.dispatcher.invoke(mockHttpRequest, mockHttpResponse);

    verify(this.developerService).update(ANY_DEVELOPER_ID, ANY_NAME);

    final Developer responseData =
        this.mapper.readValue(mockHttpResponse.getContentAsString(), Developer.class);

    assertThat(responseData).matches(d -> d.getName().equals(ANY_NAME));
  }

  @Test
  void shouldFailToUpdateDeveloperNameByIdWhenDeveloperByIdNotFound() throws Exception {
    final MockHttpRequest mockHttpRequest =
        MockHttpRequest.put("/developer/" + ANY_DEVELOPER_ID)
            .contentType(MediaType.TEXT_PLAIN)
            .content(ANY_NAME.getBytes());
    final MockHttpResponse mockHttpResponse = new MockHttpResponse();

    when(this.developerService.update(ANY_DEVELOPER_ID, ANY_NAME)).thenReturn(Optional.empty());

    this.dispatcher.invoke(mockHttpRequest, mockHttpResponse);

    verify(this.developerService).update(ANY_DEVELOPER_ID, ANY_NAME);

    assertThat(mockHttpResponse)
        .matches(res -> res.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
  }

  @Test
  void shouldCallDelete() throws Exception {
    final MockHttpRequest mockHttpRequest =
        MockHttpRequest.delete("/developer/" + ANY_DEVELOPER_ID)
            .contentType(MediaType.TEXT_PLAIN)
            .content(ANY_NAME.getBytes());
    final MockHttpResponse mockHttpResponse = new MockHttpResponse();

    this.dispatcher.invoke(mockHttpRequest, mockHttpResponse);

    verify(this.developerService).delete(ANY_DEVELOPER_ID);
  }
}
