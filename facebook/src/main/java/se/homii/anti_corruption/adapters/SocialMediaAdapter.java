package se.homii.anti_corruption.adapters;

import com.restfb.Connection;
import com.restfb.types.Post;
import com.restfb.types.User;
import se.homii.anti_corruption.facades.AccessTokenStore;
import se.homii.anti_corruption.facades.FacebookApi;
import se.homii.anti_corruption.translators.FeedTranslator;
import se.homii.domain.entity.Feed;
import se.homii.domain.valueobject.Profile;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SocialMediaAdapter implements SocialMediaGateway {

  private final FacebookApi facebookApi;
  private final FeedTranslator feedTranslator;
  private final AccessTokenStore accessTokenStore;

  @Inject
  public SocialMediaAdapter(FacebookApi facebookApi,
                            FeedTranslator feedTranslator,
                            AccessTokenStore accessTokenStore) {

    this.facebookApi = facebookApi;
    this.feedTranslator = feedTranslator;
    this.accessTokenStore = accessTokenStore;
  }


  @Override
  public void setAccessToken(String userId, String accessToken) {

    accessTokenStore.setToken(userId, accessToken);
  }

  @Override
  public Feed getFeed(String currentUserId) {

    Connection<Post> fbFeed = facebookApi.fetchFeed(
        accessTokenStore.getToken(currentUserId));
    return feedTranslator.assembleFrom(currentUserId, fbFeed);
  }

  @Override
  public Profile getProfile(String currentUserId, String userId) {

    User user = facebookApi.getUser(userId, accessTokenStore.getToken(currentUserId));

    return Profile.builder()
        .name(user.getName())
        .profilePictureUrl(user.getPicture().getUrl())
        .build();
  }
}
