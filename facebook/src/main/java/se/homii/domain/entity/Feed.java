package se.homii.domain.entity;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import se.homii.domain.valueobject.Post;

import java.util.List;

@Builder
public class Feed {

  @NonNull
  private final String owner;
  @Singular
  private final List<Post> posts;

  public String getOwner() {

    return owner;
  }

  public ImmutableList<Post> getPosts() {

    return ImmutableList.copyOf(posts);
  }
}
