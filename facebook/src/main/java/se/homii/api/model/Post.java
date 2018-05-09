package se.homii.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class Post {

  private final User from;
  private final LocalDateTime postedTimestamp;
  private final String text;
  @Singular
  private final List<Comment> comments;
}
