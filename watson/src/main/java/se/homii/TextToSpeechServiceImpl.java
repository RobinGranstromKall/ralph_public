package se.homii;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import org.apache.commons.io.FileUtils;
import se.homii.api.S3Service;
import se.homii.model.Audio;

import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class TextToSpeechServiceImpl implements TextToSpeechService {

  private final TextToSpeech textToSpeechService;
  private final S3Service s3Service;

  @Inject
  public TextToSpeechServiceImpl(S3Service s3Service) {

    this.s3Service = s3Service;
    this.textToSpeechService = new TextToSpeech(
        "Username",
        "Password");
  }

  /**
   * Creates an input stream from IBM Watson to be saved to a file and uploaded to S3.
   * @param text to translate to audio.
   * @param userId to use as directory name in S3.
   * @return Audio object with the URL to S3 and the duration of the audio file in seconds.
   * @throws IOException
   */
  @Override
  public Audio getTextToSpeechAudio(String text, String userId)
      throws IOException {

    InputStream synthesized = createAudioInputStream(text);
    File audioFile = createFile(synthesized);
    double durationInSeconds = calculateAudioDurationInSeconds(audioFile);

    String s3Url = s3Service.uploadFileForUser(userId, audioFile);

    audioFile.delete();

    return Audio.builder()
        .filePath(s3Url)
        .durationInSeconds(durationInSeconds)
        .build();
  }

  private InputStream createAudioInputStream(String text)
      throws IOException {

    SynthesizeOptions options = new SynthesizeOptions.Builder()
        .text(resolveSSML(text))
        .voice(SynthesizeOptions.Voice.EN_US_MICHAELVOICE)
        .accept(SynthesizeOptions.Accept.AUDIO_WAV)
        .build();

    return WaveUtils.reWriteWaveHeader(textToSpeechService.synthesize(options).execute());
  }

  private File createFile(InputStream inputStream)
      throws IOException {

    File audioFile = new File(UUID.randomUUID().toString() + ".wav");
    FileUtils.copyInputStreamToFile(inputStream, audioFile);
    return audioFile;
  }

  private double calculateAudioDurationInSeconds(File file) {

    double durationInSeconds = 0;

    try {
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
      AudioFormat format = audioInputStream.getFormat();
      long frames = audioInputStream.getFrameLength();
      durationInSeconds = (frames + 0.0) / format.getFrameRate();
      audioInputStream.close();
    } catch (UnsupportedAudioFileException | IOException e) {
      e.printStackTrace();
    }

    return durationInSeconds;
  }

  private String resolveSSML(String text) {

    String openingTag = "<voice-transformation type=\"Custom\" rate=\"100%\">";
    String closingTag = "</voice-transformation>";

    return openingTag + text + closingTag;
  }
}