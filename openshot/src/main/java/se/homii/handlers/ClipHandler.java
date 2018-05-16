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

import static se.homii.models.animate.Animations.*;

public class ClipHandler {

  @Inject
  private ClientHelper clientHelper;

  @Inject
  private Animations animations;

  public Double addAssetToClips(Asset asset)
      throws JsonProcessingException {

    Double clipStart = 0.0;
    Double clipLength;

    for (Post post : asset.getPosts()) {

      //Upload every post asset with animations

      Integer spotlightIndex = 1;
      List<Double> postWithCommentAudioLengths = getPostWithCommentAudioLength(post);

      // Since the api uses percentual units for position and scaling i use the resolution to get the percentual size
      Double postHeightPercent = calculatePercent(post.getImage().getHeight(), 720.0);
      Double postWidthPercent = calculatePercent(post.getImage().getWidth(), 1280.0);


      // I use the time of the audio clips to make the transitions at the right time
      Double postAudioLength = post.getAudio().getFileLength();
      clipLength = getTime(postWithCommentAudioLengths);

      // Layer is the z-index for the clip so it determines witch should be on top
      Integer layer = post.getComments()
          .size() + 11; //HACK So i can have some room to add more assets below

      Properties postProperties = makePostProperties(postWidthPercent, postHeightPercent,
          clipLength);

      addPost(
          post,
          clipStart,
          clipLength,
          layer,
          postProperties
      );

      addSpotlight(
          clipStart,
          postAudioLength,
          spotlightIndex,
          clipStart,
          clipLength
      );

      Double pastTime = postAudioLength + TRANSITION_TIME;
      layer--;
      spotlightIndex++;
      Double distanceToAnimate = postHeightPercent;
      for (int j = 0; j < post.getComments().size(); j++) {

        Comment comment = post.getComments().get(j);

        Double commentHeightPercent =
            calculatePercent(comment.getImage().getHeight(), 720.0);
        Double commentWidthPercent =
            calculatePercent(comment.getImage().getWidth(), 1280.0);

        Double commentAudioStart =
            getTimePassed(postWithCommentAudioLengths, j + 1) + clipStart;

        Properties commentProperties = makeCommentProperties(commentWidthPercent,
            commentHeightPercent, clipLength, distanceToAnimate, pastTime);

        addComment(
            comment,
            clipStart,
            clipLength,
            commentAudioStart,
            layer,
            commentProperties
        );

        addSpotlight(
            commentAudioStart,
            comment.getAudio().getFileLength(),
            spotlightIndex,
            clipStart,
            clipLength
        );


        pastTime += comment.getAudio().getFileLength();
        layer--;
        spotlightIndex++;
        distanceToAnimate += commentHeightPercent;

      }

      clipStart += getTime(postWithCommentAudioLengths);
    }
    return clipStart;
  }

  private void addPost(Post post, Double clipStart, Double clipLength, Integer layer,
                       Properties properties)
      throws JsonProcessingException {

    //Here i upload a comments audio and video clips
    Double postAudioLength = post.getAudio().getFileLength();

    uploadFileWithClip(
        post.getImage().getFileUrl(),
        clipStart,
        clipLength,
        layer,
        properties);

    uploadFileWithClip(
        post.getAudio().getFileUrl(),
        clipStart + TRANSITION_TIME,
        postAudioLength,
        layer,
        null);
  }

  private Properties makePostProperties(Double postWidthPercent, Double postHeightPercent,
                                        Double clipLength) {
    // Here i assemble all the animations for each post clip
    Properties.PropertiesBuilder postPropertiesBuilder = Properties.builder();

    postPropertiesBuilder.property(animations.centerY());
    postPropertiesBuilder.properties(
        animations.scale(postWidthPercent, postHeightPercent));
    postPropertiesBuilder.property(animations.transition(clipLength));

    return postPropertiesBuilder.build();
  }

  private void addComment(Comment comment, Double clipStart, Double clipLength,
                          Double commentAudioStart, Integer layer, Properties properties)
      throws JsonProcessingException {

    //Here i upload a comments audio and video clips
    uploadFileWithClip(
        comment.getImage().getFileUrl(),
        clipStart,
        clipLength,
        layer,
        properties);

    uploadFileWithClip(
        comment.getAudio().getFileUrl(),
        commentAudioStart,
        comment.getAudio().getFileLength(),
        layer,
        null);
  }

  private Properties makeCommentProperties(Double commentWidthPercent,
                                           Double commentHeightPercent,
                                           Double clipLength,
                                           Double distanceToAnimate,
                                           Double pastTime) {

    // Here i assemble all the animations for each comment clip

    Properties.PropertiesBuilder commentPropertiesBuilder = Properties.builder();

    commentPropertiesBuilder.property(animations.moveY(distanceToAnimate, pastTime));
    commentPropertiesBuilder.properties(
        animations.scale(commentWidthPercent, commentHeightPercent));
    commentPropertiesBuilder.property(animations.commentTransition(clipLength));

    return commentPropertiesBuilder.build();
  }

  private void addSpotlight(Double start, Double length, int spot, Double clipStart,
                            Double clipLength)
      throws JsonProcessingException {

    start = start - clipStart;

    String spotUrl = "";
    System.out.println(spot);
    switch (spot) {
      case 1:
        spotUrl = "https://gallery.yopriceville.com/var/resizes/Free-Clipart-Pictures/Decorative-Numbers/Gold_Number_Zero_PNG_Clipart_Image.png?m=1507172102";
        break;
      case 2:
        spotUrl = "https://gallery.yopriceville.com/var/resizes/Free-Clipart-Pictures/Decorative-Numbers/Gold_Number_One_PNG_Clipart_Image.png?m=1507172102";
        break;
      case 3:
        spotUrl = "https://gallery.yopriceville.com/var/resizes/Free-Clipart-Pictures/Decorative-Numbers/Gold_Number_Two_PNG_Clipart_Image.png?m=1507172102";
        break;
      case 4:
        spotUrl = "https://gallery.yopriceville.com/var/resizes/Free-Clipart-Pictures/Decorative-Numbers/Gold_Number_Three_PNG_Clipart_Image.png?m=1507172102";
        break;
    }
    Properties properties = Properties.builder()
        .property(animations.toggleAlpha(start, start + length))
        .properties(animations.scale(0.5, 0.5)) // TODO remove, just for testing
        .build();

    Double clipEnd = clipStart + clipLength;

    System.out.println("\n\n\n\n\n");
    System.out.println(clipStart);
    System.out.println(clipEnd);
    System.out.println("\n\n\n\n\n");

    uploadFileWithClip(spotUrl, clipStart, clipEnd, spot + 50, properties);

  }

  public void addStaticContent(String videoUrl, Double length)
      throws JsonProcessingException {

    //TODO add spotlight negative here

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

    return (height / resolution) * SCALE_FACTOR;
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