package lt.mazgis.issuetracker.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BUG")
public class Bug extends Issue {

  private Priority priority = Priority.MINOR;
  private BugStatus status = BugStatus.NEW;

  protected Bug() {
    this.issueType = IssueType.BUG;
  }

  public Bug(final String title, final String description, final Developer developer) {
    super(title, description, developer);
  }

  public Priority getPriority() {
    return this.priority;
  }

  public void setPriority(final Priority priority) {
    this.priority = priority;
  }

  public BugStatus getStatus() {
    return this.status;
  }

  public void setStatus(final BugStatus status) {
    this.status = status;
  }

  public Bug merge(final Bug bug) {
    super.merge(bug);
    this.priority = bug.priority;
    this.status = bug.status;
    return this;
  }
}
