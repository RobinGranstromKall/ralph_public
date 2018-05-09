package se.homii.anti_corruption.adapters;

import se.homii.domain.entity.Feed;
import se.homii.domain.valueobject.Profile;

public interface SocialMediaGateway {

  void setAccessToken(String userId, String accessToken);

  Feed getFeed(String currentUserId);

  Profile getProfile(String currentUserId, String userId);
}
