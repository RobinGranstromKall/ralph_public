package se.homii.image.api;

import se.homii.image.api.models.Asset;
import se.homii.image.model.Image;

import java.io.IOException;

public interface ImageCreator {

  Image renderFrom(Asset asset, String userId)
      throws IOException;

}
