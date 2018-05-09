package se.homii.domain.valueobject;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class WithTag {

  @NonNull
  private final String tagger;
  @NonNull
  private final String taggedUser;
}
