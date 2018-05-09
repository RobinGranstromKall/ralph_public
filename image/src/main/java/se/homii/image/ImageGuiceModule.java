package se.homii.image;

import com.google.inject.AbstractModule;
import se.homii.image.api.ImageCreator;

public class ImageGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(ImageCreator.class).to(ImageCreatorImpl.class);
  }
}
