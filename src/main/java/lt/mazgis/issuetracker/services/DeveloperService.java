package lt.mazgis.issuetracker.services;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.NotFoundException;

import org.springframework.stereotype.Component;

import lt.mazgis.issuetracker.domain.Developer;
import lt.mazgis.issuetracker.repositories.DeveloperRepository;

@Component
public class DeveloperService {

	private final DeveloperRepository developerRepository;

	public DeveloperService(final DeveloperRepository developerRepository) {
		this.developerRepository = Objects.requireNonNull(developerRepository);
	}

	public List<Developer> listOfDevelopers() {
		return this.developerRepository.findAll();
	}

	public Developer add(final Developer newDeveloper) {
		return this.developerRepository.saveAndFlush(newDeveloper);
	}

	public Developer update(final long id, final String name) {
		final Developer developer = this.developerRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User with " + id + " not found"));
		developer.setName(name);
		return this.developerRepository.saveAndFlush(developer);
	}

	public void delete(final long id) {
		this.developerRepository.deleteById(id);
	}

}
