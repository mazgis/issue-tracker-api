package lt.mazgis.issuetracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import lt.mazgis.issuetracker.domain.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}
