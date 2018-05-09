package se.homii.api.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Asset {

  private final List<Post> posts;
  private final String facebookUserId;
}
