package se.homii.texttospeech.api;

import se.homii.texttospeech.api.model.Audio;
import se.homii.texttospeech.exceptions.TextToSpeechException;

import java.io.IOException;

public interface TextToSpeechService {

  Audio getTextToSpeechAudio(String text, String userId)
      throws TextToSpeechException, IOException;
}
