package se.homii;

import com.google.inject.AbstractModule;

public class WatsonGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(TextToSpeechService.class).to(TextToSpeechServiceImpl.class);

  }
}
