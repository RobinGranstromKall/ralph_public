package se.homii;

import com.fasterxml.jackson.core.JsonProcessingException;
import se.homii.api.data.*;
import se.homii.api.query.VideoEditService;
import se.homii.handlers.ClipHandler;
import se.homii.handlers.ProjectHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class VideoEditServiceImpl implements VideoEditService {

  @Inject
  ClipHandler clipHandler;

  @Inject
  ProjectHandler projectHandler;

  public VideoEditServiceImpl() {

  }

  @Override
  public void renderVideo(Asset asset)
      throws JsonProcessingException {

    //asset = mockAsset();

    projectHandler.initializeProject(asset.getFacebookUserId());

    Double clipsLength = clipHandler.addAssetToClips(asset);
    clipHandler.addBackground("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", clipsLength);

    projectHandler.initializeProjectExport();
  }


  //TODO REMOVE
  private Asset mockAsset() {

    Audio audio1 = Audio.builder()
        .fileUrl(
            "http://www.ee.columbia.edu/~dpwe/sounds/music/africa-toto.wav")
        .fileLength(3.0)
        .build();
    Audio audio2 = Audio.builder()
        .fileUrl(
            "http://www.music.helsinki.fi/tmt/opetus/uusmedia/esim/a2002011001-e02.wav")
        .fileLength(4.0)
        .build();
    Audio audio3 = Audio.builder()
        .fileUrl(
            "http://www.ee.columbia.edu/~dpwe/sounds/music/temple_of_love-sisters_of_mercy.wav")
        .fileLength(10.0)
        .build();

    Image image1 = Image.builder()
        .fileUrl(
            "https://i.imgur.com/1jiF5F0.png")
        .height(111.0)
        .width(515.0)
        .build();
    Image image2 = Image.builder()
        .fileUrl(
            "https://i.imgur.com/zXTaSWu.png")
        .height(111.0)
        .width(515.0)
        .build();
    Image image3 = Image.builder()
        .fileUrl(
            "https://i.imgur.com/eTOh0ru.png")
        .height(111.0)
        .width(515.0)
        .build();

    Comment comment1 = Comment.builder()
        .audio(audio2)
        .image(image2)
        .order(1)
        .build();

    Comment comment2 = Comment.builder()
        .audio(audio3)
        .image(image3)
        .order(2)
        .build();

    List<Comment> comments = new ArrayList<>();
    comments.add(comment1);
    comments.add(comment2);

    Post post1 = Post.builder()
        .audio(audio1)
        .image(image1)
        .comments(comments)
        .build();

    Post post2 = Post.builder()
        .audio(audio1)
        .image(image1)
        .comments(comments)
        .build();

    List<Post> posts = new ArrayList<>();
    posts.add(post1);
    posts.add(post2);

    return Asset.builder()
        .facebookUserId("FakeFBUserID")
        .posts(posts)
        .build();

  }
}