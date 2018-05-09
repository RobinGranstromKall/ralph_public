package se.homii.image.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDimensions {

  private final int width;
  private final int height;
}
