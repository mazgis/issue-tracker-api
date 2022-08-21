package lt.mazgis.issuetracker.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Sprint {
  private final int index;
  private final List<Story> stories = new ArrayList<>();

  private int totalStoryPoints;

  public Sprint(final int index) {
    this.index = Objects.requireNonNull(index);
  }

  public void addStory(final Story story) {
    this.stories.add(Objects.requireNonNull(story));
    this.totalStoryPoints += story.getStoryPoints();
  }

  public int getTotalStoryPoints() {
    return this.totalStoryPoints;
  }

  public int getIndex() {
    return this.index;
  }

  public List<Story> getStories() {
    return Collections.unmodifiableList(this.stories);
  }
}
