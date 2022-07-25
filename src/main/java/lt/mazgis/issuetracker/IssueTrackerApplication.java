package lt.mazgis.issuetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "lt.mazgis.issuetracker")
public class IssueTrackerApplication {

	public static void main(final String[] args) {
		SpringApplication.run(IssueTrackerApplication.class, args);
	}

}
