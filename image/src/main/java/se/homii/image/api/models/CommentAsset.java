package se.homii.image.api.models;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class CommentAsset extends Asset {

  private final String fullName;
  private final String profilePictureUrl;
  private final String text;
}
