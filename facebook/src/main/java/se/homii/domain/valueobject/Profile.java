package se.homii.domain.valueobject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile {

  private final String name;
  private final String profilePictureUrl;
}
