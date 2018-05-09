package se.homii;

import se.homii.exceptions.TextToSpeechException;
import se.homii.model.Audio;

import java.io.IOException;

public interface TextToSpeechService {

  Audio getTextToSpeechAudio(String text, String userId)
      throws TextToSpeechException, IOException;
}
