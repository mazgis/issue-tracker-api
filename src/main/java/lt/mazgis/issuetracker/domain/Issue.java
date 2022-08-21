package lt.mazgis.issuetracker.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "issue_type", discriminatorType = DiscriminatorType.STRING)
public class Issue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long issueId;

  @Transient protected IssueType issueType;

  private String title;
  private String description;
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @OneToOne private Developer developer;

  protected Issue() {}

  protected Issue(final String title, final String description, final Developer developer) {
    this.title = Objects.requireNonNull(title);
    this.description = Objects.requireNonNull(description);

    this.developer = developer;
  }

  public Long getIssueId() {
    return this.issueId;
  }

  public IssueType getIssueType() {
    return this.issueType;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public OffsetDateTime getCratedAt() {
    return this.createdAt;
  }

  public void setCratedAt(final OffsetDateTime cratedAt) {
    this.createdAt = cratedAt;
  }

  public Developer getDeveloper() {
    return this.developer;
  }

  public void setDeveloper(final Developer developer) {
    this.developer = developer;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  protected void merge(final Issue issue) {
    this.description = issue.description;
    this.title = issue.title;
  }
}
