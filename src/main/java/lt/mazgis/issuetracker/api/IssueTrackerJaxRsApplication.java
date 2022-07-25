package lt.mazgis.issuetracker.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/issue-tracker")
public class IssueTrackerJaxRsApplication extends Application {

}
