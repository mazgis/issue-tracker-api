package lt.mazgis.issuetracker.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("STORY")
public class Story extends Issue {

  private int storyPoints;

  @Enumerated(EnumType.STRING)
  private StoryStatus status = StoryStatus.NEW;

  protected Story() {
    this.issueType = IssueType.STORY;
  }

  public Story(final String title, final String description, final Developer developer) {
    super(title, description, developer);
  }

  public StoryStatus getStatus() {
    return this.status;
  }

  public void setStatus(final StoryStatus status) {
    this.status = status;
  }

  public int getStoryPoints() {
    return this.storyPoints;
  }

  public void setStoryPoints(final int storyPoints) {
    this.storyPoints = storyPoints;
  }

  public Story merge(final Story issue) {
    super.merge(issue);
    this.status = issue.status;
    this.storyPoints = issue.storyPoints;
    return this;
  }
}
