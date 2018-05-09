package se.homii.models.animate.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinates {

  @JsonProperty("X")
  private Integer frame;
  @JsonProperty("Y")
  private Double value;

}
