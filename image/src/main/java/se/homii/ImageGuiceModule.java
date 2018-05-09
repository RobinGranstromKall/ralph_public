package se.homii;

import com.google.inject.AbstractModule;
import se.homii.api.ImageCreator;

public class ImageGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(ImageCreator.class).to(ImageCreatorImpl.class);
  }
}
