package se.homii.domain.service;

import com.google.common.collect.ImmutableList;
import se.homii.domain.entity.Feed;
import se.homii.domain.entity.UserScore;
import se.homii.domain.predicate.UserPredicate;
import se.homii.domain.valueobject.*;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static se.homii.domain.valueobject.Point.*;

@Singleton
public class PointCalculator {

  /**
   * This generates the top three closest friends based on a score system.
   *
   * @param feed which represents a Facebook feed.
   * @return Top three UserScores with score and facebook userId
   */
  public ImmutableList<UserScore> getTopThreeUserScores(Feed feed) {


    Map<String, UserScore> userScores = addPointsForContent(feed);

    return ImmutableList.copyOf(userScores.values().stream()
        .sorted((o1, o2) -> o2.getScore() - o1.getScore())
        .limit(3)
        .collect(Collectors.toList()));
  }

  private Map<String, UserScore> addPointsForContent(Feed feed) {

    Map<String, UserScore> userScores = new HashMap<>();

    // Add points for Post on current users feed
    addPointsForPost(userScores, feed.getOwner(), feed.getPosts());

    // Add points for likes on a post which current user has posted
    List<Like> postLikes = new ArrayList<>();
    feed.getPosts().forEach(post -> {
      if (post.getFromUserId().equals(feed.getOwner())) {
        postLikes.addAll(post.getLikes());
      }
    });
    addPointsForPostLikes(userScores, feed.getOwner(), postLikes);

    // Add points once for Comment per Post
    List<Comment> comments = new ArrayList<>();
    feed.getPosts().forEach(post -> comments.addAll(post.getComments()));
    addPointsForComment(userScores, feed.getOwner(), comments);

    // Add points for messageTags
    List<MessageTag> messageTags = new ArrayList<>();
    feed.getPosts().forEach(post -> messageTags.addAll(post.getMessageTags()));
    addPointsForMessageTags(userScores, feed.getOwner(), messageTags);

    // Add points for withTags
    List<WithTag> withTags = new ArrayList<>();
    feed.getPosts().forEach(post -> withTags.addAll(post.getWithTags()));
    addPointsForWithTags(userScores, feed.getOwner(), withTags);


    return userScores;
  }

  private void addPointsForPost(Map<String, UserScore> userScores,
                                String feedOwner,
                                List<Post> posts) {

    posts.forEach(post -> {
      if (post.getFromUserId().equals(feedOwner)) return;
      addPoints(userScores, post.getFromUserId(), POST);
    });
  }

  private void addPointsForMessageTags(Map<String, UserScore> userScores,
                                       String feedOwner,
                                       List<MessageTag> messageTags) {

    // If someone tagged feedOwner, give the tagger points
    // If feedOwner tagged someone, give the tagged user points
    messageTags.forEach(messageTag -> {
      if (messageTag.getTaggedUser().equals(feedOwner)) {
        addPoints(userScores, messageTag.getTagger(), MESSAGE_TAG);
      } else {
        addPoints(userScores, messageTag.getTaggedUser(), MESSAGE_TAG);
      }
    });
  }

  private void addPointsForWithTags(Map<String, UserScore> userScores,
                                    String feedOwner,
                                    List<WithTag> withTags) {
    // If someone tagged feedOwner, give the tagger points
    // If feedOwner tagged someone, give the tagged user points
    withTags.forEach(withTag -> {
      if (withTag.getTaggedUser().equals(feedOwner)) {
        addPoints(userScores, withTag.getTagger(), WITH_TAG);
      } else {
        addPoints(userScores, withTag.getTaggedUser(), WITH_TAG);
      }
    });
  }

  private void addPointsForPostLikes(Map<String, UserScore> userScores,
                                     String feedOwner,
                                     List<Like> postLikes) {

    postLikes.forEach(like -> {
      if (like.getFromUserId().equals(feedOwner)) return;
      addPoints(userScores, like.getFromUserId(), LIKE);
    });
  }

  private void addPointsForComment(Map<String, UserScore> userScores,
                                   String feedOwner,
                                   List<Comment> comments) {

    /* A commenter should only be rewarded once per post.
     * The predicate solves this by filtering out distinct comments on user id */
    List<Comment> distinctById = comments.stream()
        .filter(UserPredicate.distinctByKey(Comment::getFromUserId))
        .collect(Collectors.toList());

    distinctById.forEach(comment -> {
      if (comment.getFromUserId().equals(feedOwner)) return;
      addPoints(userScores, comment.getFromUserId(), COMMENT);
    });
  }


  private void addPoints(Map<String, UserScore> userScores, String userId, Point point) {

    if (userScores.containsKey(userId)) {
      userScores.get(userId).addPoints(point);
    } else {
      UserScore userScore = UserScore.builder().userId(userId).build();
      userScore.addPoints(point);
      userScores.put(userScore.getUserId(), userScore);
    }
  }
}
