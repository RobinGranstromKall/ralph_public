package se.homii.google;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import se.homii.google.api.TranslateService;

import javax.inject.Singleton;

@Singleton
public class TranslateServiceImpl implements TranslateService {

  private final Translate translate = TranslateOptions.getDefaultInstance().getService();

  @Override
  public se.homii.google.api.model.Translation translate(String text) {

    if(text == null) {
      return buildTranslation("Empty bod");
    }

    Translation translation = translate.translate(text,
        Translate.TranslateOption.targetLanguage("en"));

    return buildTranslation(translation.getTranslatedText());
  }

  private se.homii.google.api.model.Translation buildTranslation(
      String translatedText) {

    return se.homii.google.api.model.Translation
        .builder()
        .text(translatedText)
        .build();
  }
}
