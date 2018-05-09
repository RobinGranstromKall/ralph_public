package se.homii.texttospeech;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import org.apache.commons.io.FileUtils;
import se.homii.api.S3Service;
import se.homii.texttospeech.api.TextToSpeechService;
import se.homii.texttospeech.api.model.Audio;

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

    this.textToSpeechService = new TextToSpeech(
        "username",
        "password");

    this.s3Service = s3Service;
  }

  private double calculateAudioDurationInSeconds(File file) {

    double durationInSeconds = 0;
    AudioInputStream audioInputStream = null;
    try {
      audioInputStream = AudioSystem.getAudioInputStream(file);
      AudioFormat format = audioInputStream.getFormat();
      long frames = audioInputStream.getFrameLength();
      durationInSeconds = (frames + 0.0) / format.getFrameRate();
      System.out.println("Duration in seconds: " + durationInSeconds);
      audioInputStream.close();
    } catch (UnsupportedAudioFileException | IOException e) {
      e.printStackTrace();
    }
    return durationInSeconds;
  }
  
  @Override
  public Audio getTextToSpeechAudio(String text, String userId)
      throws IOException {

    if (text == null || text.isEmpty()) {
      text = "Empty post";
    }

    InputStream synthesized = createAudioInputStream(text);

    String filePath = UUID.randomUUID().toString() + ".wav";
    File audioFile = createFile(filePath, synthesized);
    double durationInSeconds = calculateAudioDurationInSeconds(audioFile);
    s3Service.uploadFileForUser(userId, audioFile);
    String s3Url = s3Service.resolveS3Url(userId, audioFile.getName());

    //TODO convert to mp3
    audioFile.delete();

    return Audio.builder()
        .filePath(s3Url)
        .durationInSeconds(durationInSeconds)
        .build();
  }

  private InputStream createAudioInputStream(String text)
      throws IOException {

    SynthesizeOptions options = new SynthesizeOptions.Builder()
        .text(transformText(text))
        .voice(SynthesizeOptions.Voice.EN_US_MICHAELVOICE)
        .accept(SynthesizeOptions.Accept.AUDIO_WAV)
        .build();

    return WaveUtils.reWriteWaveHeader(textToSpeechService.synthesize(options).execute());
  }

  private File createFile(String filePath, InputStream inputStream)
      throws IOException {

    File audioFile = new File(filePath);
    FileUtils.copyInputStreamToFile(inputStream, audioFile);
    return audioFile;
  }

  private String transformText(String text) {
    return String.format(
        "<voice-transformation type=\"Custom\" rate=\"fast\"> %s </voice-transformation>",
        //TODO fix
        text.substring(0, Math.min(70, (text.length() - 1))));
  }
}