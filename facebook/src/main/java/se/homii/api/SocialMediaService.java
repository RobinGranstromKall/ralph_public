package se.homii.api;

import se.homii.api.model.Feed;

public interface SocialMediaService {

  Feed getFeedWithTopThreeFriends(String currentUserId, String accessToken);
}
