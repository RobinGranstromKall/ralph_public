package se.homii.api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentAsset extends Asset {

  private final String fullName;
  private final String profilePictureUrl;
  private final String text;
}
