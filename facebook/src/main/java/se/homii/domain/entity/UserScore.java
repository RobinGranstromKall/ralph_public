package se.homii.domain.entity;

import lombok.Builder;
import lombok.NonNull;
import se.homii.domain.valueobject.Point;

@Builder
public class UserScore {

  @NonNull
  private final String userId;
  private int score;

  public void addPoints(Point point) {

    this.score += point.getValue();
  }

  public int getScore() {

    return score;
  }

  public String getUserId() {

    return userId;
  }
}
