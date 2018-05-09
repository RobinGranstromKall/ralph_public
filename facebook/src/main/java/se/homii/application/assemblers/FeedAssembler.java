package se.homii.application.assemblers;

import com.google.common.collect.ImmutableList;
import se.homii.anti_corruption.adapters.SocialMediaGateway;
import se.homii.api.model.Comment;
import se.homii.api.model.Feed;
import se.homii.api.model.Post;
import se.homii.api.model.User;
import se.homii.domain.valueobject.Profile;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedAssembler {

  private final SocialMediaGateway socialMediaGateway;
  private final int MAX_POSTS_PER_FEED = 5;
  private final int MAX_COMMENTS_PER_POST = 3;

  @Inject
  public FeedAssembler(SocialMediaGateway socialMediaGateway) {

    this.socialMediaGateway = socialMediaGateway;
  }

  public Feed assembleFrom(se.homii.domain.entity.Feed feed,
                           String currentUserId,
                           List<String> friendUserIds) {

    Feed.FeedBuilder feedBuilder = Feed.builder();

    Map<String, User> topThreeFriends = assembleTopThreeFriends(currentUserId,
        friendUserIds);
    feedBuilder.topThreeFriends(topThreeFriends.values());


    Map<String, User> participants = getParticipants(topThreeFriends, currentUserId);

    List<Post> posts = assemblePosts(feed.getPosts(), topThreeFriends, participants);
    feedBuilder.posts(posts);

    return feedBuilder.build();
  }

  private Map<String, User> assembleTopThreeFriends(String currentUserId,
                                                    List<String> friendUserIds) {

    Map<String, User> friends = new HashMap<>();
    friendUserIds.forEach(id -> {

      Profile profile = socialMediaGateway.getProfile(currentUserId, id);

      friends.put(id, User.builder()
          .name(profile.getName())
          .profilePictureUrl(profile.getProfilePictureUrl())
          .build());
    });
    return friends;
  }

  private Map<String, User> getParticipants(Map<String, User> friends,
                                            String currentUserId) {

    Profile profile = socialMediaGateway.getProfile(currentUserId, currentUserId);

    User currentUser = User.builder()
        .name(profile.getName())
        .profilePictureUrl(profile.getProfilePictureUrl())
        .build();

    Map<String, User> participants = new HashMap<>(friends);
    participants.put(currentUserId, currentUser);
    return participants;
  }

  private List<Post> assemblePosts(List<se.homii.domain.valueobject.Post> posts,
                                   Map<String, User> friends,
                                   Map<String, User> participants) {

    List<Post> assembledPosts = new ArrayList<>();

    for (se.homii.domain.valueobject.Post post : posts) {
      if (assembledPosts.size() == MAX_POSTS_PER_FEED) break;

      // If post doesn't have comments friends, or message is empty, skip it
      if (!hasCommentFromAFriend(friends, post) || !isValidMessage(post.getMessage())) {
        continue;
      }

      Post.PostBuilder postBuilder = Post.builder();
      postBuilder.from(participants.get(post.getFromUserId()));
      postBuilder.text(post.getMessage());
      postBuilder.postedTimestamp(post.getCreatedTime());

      // Add comments to post
      List<Comment> comments = assembleComments(participants, post.getComments());
      postBuilder.comments(comments);

      assembledPosts.add(postBuilder.build());
    }

    return assembledPosts;
  }

  private boolean hasCommentFromAFriend(Map<String, User> friends,
                                        se.homii.domain.valueobject.Post post) {

    return post.getComments()
        .stream()
        .anyMatch(comment -> friends.containsKey(comment.getFromUserId()));
  }

  private List<Comment> assembleComments(Map<String, User> participants,
                                         ImmutableList<se.homii.domain.valueobject.Comment> comments) {

    List<Comment> assembledComments = new ArrayList<>();

    for (se.homii.domain.valueobject.Comment comment : comments) {
      if (assembledComments.size() == MAX_COMMENTS_PER_POST) break;

      // Only add valid comments from participants
      if (participants.containsKey(comment.getFromUserId())
          && isValidMessage(comment.getMessage())) {

        assembledComments.add(
            Comment.builder()
                .from(participants.get(comment.getFromUserId()))
                .text(comment.getMessage())
                .postedTimestamp(comment.getCreated())
                .build()
        );
      }
    }

    return assembledComments;
  }

  private boolean isValidMessage(String message) {

    return message != null;
  }
}
