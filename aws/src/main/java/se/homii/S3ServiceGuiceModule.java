package se.homii;

import com.google.inject.AbstractModule;
import se.homii.api.S3Service;

public class S3ServiceGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(S3Service.class).to(S3ServiceImpl.class);
  }
}
