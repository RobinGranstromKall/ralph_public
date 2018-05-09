package se.homii.domain.valueobject;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Like {

  @NonNull
  private final String fromUserId;
}
