package se.homii;

import com.google.inject.AbstractModule;
import se.homii.api.query.VideoEditService;

public class OpenshotGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(VideoEditService.class).to(VideoEditServiceImpl.class);
  }
}
