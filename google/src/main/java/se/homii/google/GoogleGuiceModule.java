package se.homii.google;

import com.google.inject.AbstractModule;
import se.homii.google.api.TranslateService;

public class GoogleGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(TranslateService.class).to(TranslateServiceImpl.class);
  }

}
