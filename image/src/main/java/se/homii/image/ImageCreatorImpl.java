package se.homii.image;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import se.homii.S3ServiceImpl;
import se.homii.api.S3Service;
import se.homii.image.api.ImageCreator;
import se.homii.image.api.models.Asset;
import se.homii.image.api.models.CommentAsset;
import se.homii.image.api.models.PostAsset;
import se.homii.image.model.ImageDimensions;
import se.homii.image.model.Image;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.UUID;

public class ImageCreatorImpl implements ImageCreator {

  private final Template postTemplate;
  private final Template commentTemplate;
  private final S3Service s3Service;

  @Inject
  public ImageCreatorImpl(S3Service s3Service)
      throws IOException {

    Handlebars handlebars = new Handlebars();
    this.postTemplate = handlebars.compile("handlebars/facebook-post.template");
    this.commentTemplate = handlebars.compile("handlebars/facebook-comment.template");
    this.s3Service = s3Service;
  }

  public static void main(String[] args)
      throws IOException {

    ImageCreatorImpl imageCreator = new ImageCreatorImpl(new S3ServiceImpl());

    PostAsset post = PostAsset.builder()
        .text("Hej hej, det har ar en text post")
        .profilePictureUrl("url")
        .fullName("Markus Averheim")
        .postedTimestamp(
            LocalDateTime.now())
        .build();

    CommentAsset comment = CommentAsset.builder()
        .text("Hej hej, det har ar en text post")
        .profilePictureUrl("url")
        .fullName("Markus Averheim")
        .build();

    imageCreator.renderFrom(post, "123");
  }

  @Override
  public Image renderFrom(Asset asset, String userId)
      throws IOException {

    File htmlFile = createHtmlFileFrom(asset);
    File image = renderImage(htmlFile);

    cropImage(image);

    ImageDimensions imageDimensions = getImageDimensions(image);
    System.out.println("IMG DIMENSIONS: " + imageDimensions.toString());

    //String s3Url = uploadToS3(userId, image);
    //deleteLocalFiles(htmlFile, image);

    return Image.builder()
        //.filePath(s3Url)
        .imageDimensions(imageDimensions)
        .build();
  }

  private void cropImage(File image)
      throws IOException {

    BufferedImage read = ImageIO.read(image);
    BufferedImage bufferedImage = removeTransparentPixels(read);
    ImageIO.write(bufferedImage, "png", image);
  }

  private BufferedImage removeTransparentPixels(BufferedImage image) {

    WritableRaster raster = image.getAlphaRaster();
    int width = raster.getWidth();
    int height = raster.getHeight();
    int left = 0;
    int top = 0;
    int right = width - 1;
    int bottom = height - 1;
    int minRight = width - 1;
    int minBottom = height - 1;

    top:
    for (; top < bottom; top++) {
      for (int x = 0; x < width; x++) {
        if (raster.getSample(x, top, 0) != 0) {
          minRight = x;
          minBottom = top;
          break top;
        }
      }
    }

    left:
    for (; left < minRight; left++) {
      for (int y = height - 1; y > top; y--) {
        if (raster.getSample(left, y, 0) != 0) {
          minBottom = y;
          break left;
        }
      }
    }

    bottom:
    for (; bottom > minBottom; bottom--) {
      for (int x = width - 1; x >= left; x--) {
        if (raster.getSample(x, bottom, 0) != 0) {
          minRight = x;
          break bottom;
        }
      }
    }

    right:
    for (; right > minRight; right--) {
      for (int y = bottom; y >= top; y--) {
        if (raster.getSample(right, y, 0) != 0) {
          break right;
        }
      }
    }

    return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
  }

  private String uploadToS3(String userId, File image) {

    s3Service.uploadFileForUser(userId, image);
    return s3Service.resolveS3Url(userId, image.getName());
  }

  private void deleteLocalFiles(File... files) {

    for (File file : files) {
      file.delete();
    }
  }

  private File createHtmlFileFrom(Asset asset)
      throws IOException {

    String html = null;

    if (asset instanceof PostAsset) {
      html = postTemplate.apply(asset);
    } else if (asset instanceof CommentAsset) {
      html = commentTemplate.apply(asset);
    }

    return saveToFile(html);
  }

  private ImageDimensions getImageDimensions(File file) {

    BufferedImage bufferedImage = null;
    try {
      bufferedImage = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();

    return ImageDimensions.builder()
        .width(width)
        .height(height)
        .build();
  }

  private File renderImage(File file) {

    String destinationUrl = UUID.randomUUID().toString() + ".png";

    try {

      Process process = Runtime.getRuntime()
          .exec(
              "wkhtmltoimage --transparent --format png " + file.getAbsolutePath() + " " + destinationUrl);
      process.waitFor();

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return new File(destinationUrl);
  }

  private File saveToFile(String html)
      throws FileNotFoundException {

    File file = new File(UUID.randomUUID().toString() + ".html");

    PrintWriter printWriter = new PrintWriter(file);
    printWriter.print(html);
    printWriter.flush();
    printWriter.close();

    return file;
  }
}
