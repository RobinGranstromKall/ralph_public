package se.homii.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Audio {

  private final String filePath;
  private final double durationInSeconds;

}
