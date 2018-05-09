package se.homii.api.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostAsset extends Asset {

  private final String fullName;
  private final String profilePictureUrl;
  private final LocalDateTime postedTimestamp;
  private final String text;
}
