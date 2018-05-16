package se.homii.image.api.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class PostAsset extends Asset {

  private final String fullName;
  private final String profilePictureUrl;
  private final LocalDateTime postedTimestamp;
  private final String text;
}
