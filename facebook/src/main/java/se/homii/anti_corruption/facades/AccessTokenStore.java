package se.homii.anti_corruption.facades;

public interface AccessTokenStore {

  void setToken(String userId, String token);

  String getToken(String userId);
}
