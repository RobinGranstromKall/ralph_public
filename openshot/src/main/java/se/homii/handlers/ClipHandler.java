package se.homii.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.homii.ClientHelper;
import se.homii.api.data.*;
import se.homii.models.animate.Animations;
import se.homii.models.animate.property.Properties;
import se.homii.models.request.Clip;
import se.homii.models.request.File;
import se.homii.models.response.ClipResponse;
import se.homii.models.response.FileResponse;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static se.homii.models.animate.Animations.PAUSE_TIME;
import static se.homii.models.animate.Animations.TRANSITION_TIME;

public class ClipHandler {

  @Inject
  private ClientHelper clientHelper;

  @Inject
  private Animations animations;

  /***
   * This makes video
   * @param asset
   * @return Video
   * @throws JsonProcessingException
   */
  public Double addAssetToClips(Asset asset)
      throws JsonProcessingException {

    Double clipStart = 0.0;
    Double clipEnd;

    for (Post post : asset.getPosts()) {
      //Upload every post asset with animations
      List<Double> postWithCommentAudioLengths = getPostWithCommentAudioLength(post);

      Properties.PropertiesBuilder postPropertiesBuilder = Properties.builder();

      // Since the api uses percentual units for position and scaling i use the resolution to get the percentual size
      Double postHeightPercent = calculatePercent(post.getImage().getHeight(), 720.0);
      Double postWidthPercent = calculatePercent(post.getImage().getWidth(), 1280.0);

      // I use the time of the audio clips to make the transitions at the right time
      Double postAudioLength = post.getAudio().getFileLength();
      clipEnd = getTime(postWithCommentAudioLengths);

      // Layer is the z-index for the clip so it determines witch should be on top
      Integer layer = post.getComments()
          .size() + 11; //HACK So i can have some room to add more assets below

      // Here i do the animations and scaling
      postPropertiesBuilder.property(animations.centerY());
      postPropertiesBuilder.properties(
          animations.scale(postWidthPercent, postHeightPercent));
      postPropertiesBuilder.property(animations.transition(clipEnd));

      // Here i upload the image files with the animations
      uploadFileWithClip(
          post.getImage().getFileUrl(),
          clipStart,
          clipEnd,
          layer,
          postPropertiesBuilder.build());

      // Here i upload the audio files
      uploadFileWithClip(
          post.getAudio().getFileUrl(),
          clipStart + TRANSITION_TIME,
          postAudioLength,
          layer,
          null);
      //Here i do some incrementation so that the timing of the animations is right.
      Double pastTime = postAudioLength + TRANSITION_TIME;
      layer--;
      Double distanceToAnimate = postHeightPercent;
      for (int j = 0; j < post.getComments().size(); j++) {


        // This whole part is the same as the one above just with comments
        Comment comment = post.getComments().get(j);
        Audio commentAudio = comment.getAudio();
        Image commentImage = comment.getImage();

        Double commentHeightPercent =
            calculatePercent(comment.getImage().getHeight(), 720.0);
        Double commentWidthPercent =
            calculatePercent(comment.getImage().getWidth(), 1280.0);

        Double commentAudioStart =
            getTimePassed(postWithCommentAudioLengths, j + 1) + clipStart;

        Properties.PropertiesBuilder commentPropertiesBuilder = Properties.builder();
        // Comments have one more animations as of this point that's the moveY() it makes the comment fall down to a specific place at a specific time
        commentPropertiesBuilder.property(animations.moveY(distanceToAnimate, pastTime));
        commentPropertiesBuilder.properties(
            animations.scale(commentWidthPercent, commentHeightPercent));
        commentPropertiesBuilder.property(animations.commentTransition(clipEnd));

        uploadFileWithClip(
            commentImage.getFileUrl(),
            clipStart,
            clipEnd,
            layer,
            commentPropertiesBuilder.build());

        uploadFileWithClip(
            commentAudio.getFileUrl(),
            commentAudioStart,
            commentAudio.getFileLength(),
            layer,
            null);

        pastTime += commentAudio.getFileLength();
        layer--;
        distanceToAnimate += commentHeightPercent;

      }

      clipStart += getTime(postWithCommentAudioLengths);
    }
    return clipStart;
  }

  public void addBackground(String videoUrl, Double length)
      throws JsonProcessingException {

    // Here i add the background clip that in the future will hold all the static animations.
    uploadFileWithClip(videoUrl, 0.0, length, 1, null);

  }

  private ClipResponse uploadFileWithClip(String fileUrl, Double position, Double end,
                                          Integer layer, Properties properties)
      throws JsonProcessingException {

    FileResponse fileResponse = uploadFile(fileUrl);

    return properties == null ?
        addClipToFile(fileResponse.getUrl(), position, end, layer) :
        addClipToFile(fileResponse.getUrl(), position, end, layer, properties);

  }

  private FileResponse uploadFile(String fileUrl) {

    File file = File.builder()
        .media(null)
        .project(clientHelper.getProjectUrl())
        .json("{\"url\": \"" + fileUrl + "\"}") // TODO Do better
        .build();

    Response response = clientHelper.buildClientWithHeader("files/", null)
        .buildPost(clientHelper.buildEntity(file))
        .invoke();

    return response.readEntity(FileResponse.class);
  }

  private ClipResponse addClipToFile(String fileUrl, Double position, Double end,
                                     Integer layer)
      throws JsonProcessingException {

    Properties properties = Properties.builder()
        .build();

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(properties);

    Clip clip = Clip.builder()
        .file(fileUrl)
        .project(clientHelper.getProjectUrl())
        .position(position)
        .start(0)
        .end(end)
        .layer(layer)
        .json(json)
        .build();

    return clientHelper.buildClientWithHeader("clips/", null)
        .buildPost(clientHelper.buildEntity(clip))
        .invoke()
        .readEntity(ClipResponse.class);
  }

  private ClipResponse addClipToFile(String fileUrl, Double position, Double end,
                                     Integer layer, Properties properties)
      throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();

    String json = objectMapper.writeValueAsString(properties);

    Clip clip = Clip.builder()
        .file(fileUrl)
        .project(clientHelper.getProjectUrl())
        .position(position)
        .start(0)
        .end(end)
        .layer(layer)
        .json(json)
        .build();

    return clientHelper.buildClientWithHeader("clips/", null)
        .buildPost(clientHelper.buildEntity(clip))
        .invoke()
        .readEntity(ClipResponse.class);
  }

  private Double calculatePercent(Double height, Double resolution) {

    return height / resolution;
  }

  private Double getTime(List<Double> list) {

    Double totalLength = 0.0;

    for (Double assetLength : list) {
      totalLength += assetLength;
    }

    return totalLength;
  }

  private Double getRemainingTime(List<Double> list, int startIndex) {

    Double length = 0.0;

    for (int i = startIndex; i < list.size(); i++) {
      length += list.get(i);
    }

    return length;
  }

  private Double getTimePassed(List<Double> list, int startIndex) {

    return getTime(list) - getRemainingTime(list, startIndex);
  }

  private List<Double> getPostWithCommentAudioLength(Post post) {

    List<Double> audioLengths = new ArrayList<>();
    audioLengths.add(post.getAudio().getFileLength() + PAUSE_TIME + TRANSITION_TIME);

    for (Comment comment : post.getComments()) {

      audioLengths.add(comment.getAudio().getFileLength() + PAUSE_TIME);
    }

    return audioLengths;
  }
}