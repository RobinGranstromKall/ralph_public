package se.homii.models.animate.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public class ScaleX implements Property {

  @Singular
  @JsonProperty("Points")
  List<Point> points;

  @Override
  public List<Point> getPoints() {

    return points;
  }
}
