package se.homii.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Feed {

  @Singular
  private final List<Post> posts;
  @Singular
  private final List<User> topThreeFriends;
}
