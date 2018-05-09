package se.homii.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Image {

  private final String filePath;
  private final ImageDimensions imageDimensions;
}
