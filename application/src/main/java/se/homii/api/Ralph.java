package se.homii.api;

import se.homii.texttospeech.exceptions.TextToSpeechException;

import java.io.IOException;

public interface Ralph {

  public RenderResult generateTest(String userId, String accessToken)
      throws TextToSpeechException, IOException;

}
