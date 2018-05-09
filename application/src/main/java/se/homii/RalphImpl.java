package se.homii;

import se.homii.api.Ralph;
import se.homii.api.RenderResult;
import se.homii.api.SocialMediaService;
import se.homii.api.data.Asset;
import se.homii.api.model.Comment;
import se.homii.api.model.Feed;
import se.homii.api.model.Post;
import se.homii.api.query.VideoEditService;
import se.homii.image.api.ImageCreator;
import se.homii.image.api.models.CommentAsset;
import se.homii.image.api.models.PostAsset;
import se.homii.image.model.Image;
import se.homii.texttospeech.api.TextToSpeechService;
import se.homii.texttospeech.api.model.Audio;
import se.homii.texttospeech.exceptions.TextToSpeechException;
import se.homii.google.api.TranslateService;
import se.homii.google.api.model.Translation;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RalphImpl implements Ralph {

  @Inject
  SocialMediaService socialMediaService;

  @Inject
  TranslateService translateService;

  @Inject
  TextToSpeechService textToSpeechService;

  @Inject
  ImageCreator imageCreator;

  @Inject
  VideoEditService videoEditService;

  @Override
  public RenderResult generateTest(String userId, String accessToken)
      throws TextToSpeechException, IOException {

    Feed feedWithTopThreeFriends = socialMediaService.getFeedWithTopThreeFriends(userId,
        accessToken);

    List<se.homii.api.data.Post> posts = new ArrayList<>();

    for (Post post : feedWithTopThreeFriends.getPosts()) {

      List<se.homii.api.data.Comment> comments = new ArrayList<>();

      for (Comment comment : post.getComments()) {

        se.homii.api.data.Comment commentAsset = getComment(comment, userId);

        comments.add(commentAsset);
      }
      se.homii.api.data.Post postAsset = getPost(post, comments, userId);

      posts.add(postAsset);
    }

    Asset asset = Asset.builder()
        .posts(posts)
        .build();

    videoEditService.renderVideo(asset);

    return null;
  }

  private se.homii.api.data.Post getPost(Post post,
                                         List<se.homii.api.data.Comment> comments, String userId)
      throws TextToSpeechException, IOException {

    Translation translatedPost = translateService.translate(post.getText());

    Audio textToSpeechAudio = textToSpeechService.getTextToSpeechAudio(
        translatedPost.getText(), userId);

    PostAsset assembledPost = PostAsset.builder()
        .fullName(post.getFrom().getName())
        .profilePictureUrl(post.getFrom().getProfilePictureUrl())
        .postedTimestamp(post.getPostedTimestamp())
        .text(translatedPost.getText())
        .build();

    Image image = imageCreator.renderFrom(assembledPost, userId);

    //TODO publish post image

    return se.homii.api.data.Post.builder()
        .image(se.homii.api.data.Image.builder()
            .fileUrl(image.getFilePath())
            .height((double) image.getImageDimensions().getHeight())
            .width((double) image.getImageDimensions().getWidth())
            .build())
        .audio(se.homii.api.data.Audio.builder()
            .fileUrl(textToSpeechAudio.getFilePath())
            .fileLength(textToSpeechAudio.getDurationInSeconds())
            .build())
        .comments(comments)
        .build();
  }

  private se.homii.api.data.Comment getComment(Comment comment, String userId)
      throws TextToSpeechException, IOException {

    Translation translatedComment = translateService.translate(comment.getText());

    Audio textToSpeechAudio = textToSpeechService.getTextToSpeechAudio(
        translatedComment.getText(), userId);

    CommentAsset assembledComment = CommentAsset.builder()
        .fullName(comment.getFrom().getName())
        .profilePictureUrl(comment.getFrom().getProfilePictureUrl())
        .text(translatedComment.getText())
        .build();

    Image commentImage = imageCreator.renderFrom(assembledComment, userId);

    //TODO publish comment image

    return se.homii.api.data.Comment.builder()
        .image(se.homii.api.data.Image.builder()
            .fileUrl(commentImage.getFilePath())
            .height((double) commentImage.getImageDimensions().getHeight())
            .width((double) commentImage.getImageDimensions().getWidth())
            .build())
        .audio(se.homii.api.data.Audio.builder()
            .fileUrl(textToSpeechAudio.getFilePath())
            .fileLength(textToSpeechAudio.getDurationInSeconds())
            .build())
        .build();
  }
}
