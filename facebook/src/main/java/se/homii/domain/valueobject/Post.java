package se.homii.domain.valueobject;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.time.LocalDateTime;

@Data
@Builder
public class Post {

  @NonNull
  private final String fromUserId;
  private final String message;
  @NonNull
  private final LocalDateTime createdTime;
  @Singular
  private final ImmutableList<Like> likes;
  @Singular
  private final ImmutableList<Comment> comments;
  @Singular
  private final ImmutableList<WithTag> withTags;
  @Singular
  private final ImmutableList<MessageTag> messageTags;
}
