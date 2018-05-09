package se.homii;

import com.google.inject.AbstractModule;
import se.homii.anti_corruption.adapters.SocialMediaAdapter;
import se.homii.anti_corruption.adapters.SocialMediaGateway;
import se.homii.anti_corruption.facades.AccessTokenStore;
import se.homii.anti_corruption.facades.FacebookApi;
import se.homii.anti_corruption.facades.FacebookApiImpl;
import se.homii.anti_corruption.facades.SimpleAccessTokenStore;
import se.homii.api.SocialMediaService;
import se.homii.application.SocialMediaServiceImpl;

public class SocialMediaGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(SocialMediaService.class).to(SocialMediaServiceImpl.class);
    bind(FacebookApi.class).to(FacebookApiImpl.class);
    bind(SocialMediaGateway.class).to(SocialMediaAdapter.class);
    bind(AccessTokenStore.class).to(SimpleAccessTokenStore.class);
  }
}
