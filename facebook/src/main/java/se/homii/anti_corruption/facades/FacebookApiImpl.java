package se.homii.anti_corruption.facades;

import com.restfb.*;
import com.restfb.types.Post;
import com.restfb.types.User;

import javax.inject.Singleton;

@Singleton
public class FacebookApiImpl implements FacebookApi {

  @Override
  public Connection<Post> fetchFeed(String token) {

    FacebookClient client = createFacebookClient(token);

    String fields = String.format("%s,%s,%s,%s,%s,%s,%s"
        , "from"
        , "message"
        , "likes{id, name}"
        , "created_time"
        , "with_tags"
        , "message_tags"
        , "comments{from,message,created_time,message_tags}");

    int pageLimit = 1000;

    return client.fetchConnection("me/feed",
        Post.class,
        Parameter.with("limit", pageLimit),
        Parameter.with("fields", fields));
  }

  @Override
  public User getUser(String userId, String token) {

    FacebookClient client = createFacebookClient(token);

    String fields = String.format("%s, %s, %s"
        , "id"
        , "name"
        , "picture{url}");

    return client.fetchObject(userId, User.class, Parameter.with("fields", fields));
  }

  private DefaultFacebookClient createFacebookClient(String token) {

    return new DefaultFacebookClient(token, Version.VERSION_2_12);
  }
}
