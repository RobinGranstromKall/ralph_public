package se.homii.domain.valueobject;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@Builder
public class Comment {

  @NonNull
  private final String fromUserId;
  @NonNull
  private final String message;
  @NonNull
  private final LocalDateTime created;
}
