package se.homii.application;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import se.homii.anti_corruption.adapters.SocialMediaGateway;
import se.homii.api.SocialMediaService;
import se.homii.api.model.Comment;
import se.homii.api.model.Feed;
import se.homii.api.model.Post;
import se.homii.application.assemblers.FeedAssembler;
import se.homii.domain.entity.UserScore;
import se.homii.domain.service.PointCalculator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class SocialMediaServiceImpl implements SocialMediaService {

  private final PointCalculator pointCalculator;
  private final FeedAssembler feedAssembler;
  private final SocialMediaGateway socialMediaGateway;

  @Inject
  public SocialMediaServiceImpl(PointCalculator pointCalculator,
                                FeedAssembler feedAssembler,
                                SocialMediaGateway socialMediaGateway) {

    this.pointCalculator = pointCalculator;
    this.feedAssembler = feedAssembler;
    this.socialMediaGateway = socialMediaGateway;
  }

  @Override
  public Feed getFeedWithTopThreeFriends(String currentUserId, String accessToken) {

    socialMediaGateway.setAccessToken(currentUserId, accessToken);

    se.homii.domain.entity.Feed feed = socialMediaGateway.getFeed(currentUserId);

    ImmutableList<UserScore> topThreeUserScores = pointCalculator.getTopThreeUserScores(
        feed);

    List<String> topThreeFriendsIds = new ArrayList<>();
    topThreeUserScores.forEach(
        userScore -> topThreeFriendsIds.add(userScore.getUserId()));

    Feed assembledFeed = feedAssembler.assembleFrom(feed, currentUserId,
        topThreeFriendsIds);

    countChars(assembledFeed);

    return assembledFeed;
  }

  private void countChars(Feed assembledFeed) {

    int chars = 0;

    for (Post post : assembledFeed.getPosts()) {
      chars += post.getText().length();
      for (Comment comment : post.getComments()) {
        chars += comment.getText().length();
      }
    }

    System.out.println("TOTAL CHARS: " + chars);
  }
}