package se.homii.anti_corruption.facades;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Slf4j
public class SimpleAccessTokenStore implements AccessTokenStore {

  private Map<String, String> tokens = new HashMap<>();

  @Override
  public void setToken(String userId, String accessToken) {

    log.debug("Storing access token for user {}", userId);
    tokens.put(userId, accessToken);
  }

  @Override
  public String getToken(String userId) {

    return tokens.get(userId);
  }
}
