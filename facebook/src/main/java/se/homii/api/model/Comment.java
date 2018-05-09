package se.homii.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
public class Comment {

  private final User from;
  private final LocalDateTime postedTimestamp;
  private final String text;
}
