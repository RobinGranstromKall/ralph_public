package se.homii.texttospeech;

import com.google.inject.AbstractModule;
import se.homii.texttospeech.api.TextToSpeechService;

public class WatsonGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(TextToSpeechService.class).to(TextToSpeechServiceImpl.class);

  }
}
