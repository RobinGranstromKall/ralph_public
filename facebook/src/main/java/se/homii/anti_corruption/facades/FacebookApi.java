package se.homii.anti_corruption.facades;

import com.restfb.Connection;
import com.restfb.types.Post;
import com.restfb.types.User;

public interface FacebookApi {

  Connection<Post> fetchFeed(String token);

  User getUser(String id, String token);
}
