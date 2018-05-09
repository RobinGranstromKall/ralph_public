package se.homii.models.animate.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Point {

  @JsonProperty("co")
  private Coordinates coordinates;
  @JsonProperty("interpolation")
  private Integer interpolation;

}