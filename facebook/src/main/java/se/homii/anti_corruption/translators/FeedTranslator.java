package se.homii.anti_corruption.translators;

import com.restfb.Connection;
import com.restfb.types.Likes;
import com.restfb.types.NamedFacebookType;
import se.homii.domain.entity.Feed;
import se.homii.domain.valueobject.*;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.List;

@Singleton
public class FeedTranslator {

  public Feed assembleFrom(String currentUserId,
                           Connection<com.restfb.types.Post> feed) {

    Feed.FeedBuilder feedBuilder = Feed.builder();
    for (List<com.restfb.types.Post> fbPosts : feed) {
      for (com.restfb.types.Post fbPost : fbPosts) {
        if (fbPost.getFrom() == null) continue;

        Post.PostBuilder postBuilder = Post.builder();
        postBuilder.fromUserId(fbPost.getFrom().getId());
        postBuilder.message(fbPost.getMessage());
        postBuilder.createdTime(
            new Timestamp(fbPost.getCreatedTime().getTime()).toLocalDateTime());

        // Add post comments
        if (fbPost.getComments() != null) {
          for (com.restfb.types.Comment comment : fbPost.getComments().getData()) {
            if (comment.getFrom() == null) continue;

            postBuilder.comment(Comment.builder()
                .fromUserId(comment.getFrom().getId())
                .message(comment.getMessage())
                .created(
                    new Timestamp(comment.getCreatedTime().getTime()).toLocalDateTime())
                .build());
          }
        }

        // Add post likes
        if (fbPost.getLikes() != null) {
          for (Likes.LikeItem like : fbPost.getLikes().getData()) {
            postBuilder.like(
                Like.builder()
                    .fromUserId(like.getId())
                    .build());
          }
        }

        // Add post message_tags
        for (com.restfb.types.MessageTag messageTag : fbPost.getMessageTags()) {
          postBuilder.messageTag(
              MessageTag.builder()
                  .tagger(fbPost.getFrom().getId())
                  .taggedUser(messageTag.getId())
                  .build());
        }

        // Add post with_tags
        for (NamedFacebookType withTag : fbPost.getWithTags()) {
          postBuilder.withTag(
              WithTag.builder()
                  .tagger(fbPost.getFrom().getId())
                  .taggedUser(withTag.getId())
                  .build()
          );
        }

        feedBuilder
            .owner(currentUserId)
            .post(postBuilder.build());
      }
    }

    return feedBuilder.build();
  }
}
