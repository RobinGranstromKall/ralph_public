package se.homii.api.data;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class Image {

  private final String fileUrl;
  private final Double height;
  private final Double width;
}
