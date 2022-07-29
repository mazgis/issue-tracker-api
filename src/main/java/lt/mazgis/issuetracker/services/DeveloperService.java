package lt.mazgis.issuetracker.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.repositories.DeveloperRepository;

@Component
public class DeveloperService {

  private final DeveloperRepository developerRepository;

  public DeveloperService(final DeveloperRepository developerRepository) {
    this.developerRepository = Objects.requireNonNull(developerRepository);
  }

  public List<Developer> listAllDevelopers() {
    return this.developerRepository.findAll();
  }

  public Developer add(final Developer newDeveloper) {
    return this.developerRepository.saveAndFlush(newDeveloper);
  }

  public Optional<Developer> update(final long id, final String name) {
    return this.developerRepository
        .findById(id)
        .map(d -> updateDeveloperName(name, d))
        .map(this.developerRepository::saveAndFlush);
  }

  private Developer updateDeveloperName(final String name, final Developer d) {
    d.setName(name);
    return d;
  }

  public void delete(final long id) {
    this.developerRepository.deleteById(id);
  }

  public Optional<Developer> getDeveloper(final long developerId) {
    return this.developerRepository.findById(developerId);
  }
}
