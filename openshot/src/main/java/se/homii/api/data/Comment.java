package se.homii.api.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {

  private final Audio audio;
  private final Image image;
  private final Integer order;
}
