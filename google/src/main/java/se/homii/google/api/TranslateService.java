package se.homii.google.api;


import se.homii.google.api.model.Translation;

public interface TranslateService {

  Translation translate(String text);

}
