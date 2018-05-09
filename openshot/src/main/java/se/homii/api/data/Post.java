package se.homii.api.data;


import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class Post {

  private final Audio audio;
  private final Image image;
  private final List<Comment> comments;
}
