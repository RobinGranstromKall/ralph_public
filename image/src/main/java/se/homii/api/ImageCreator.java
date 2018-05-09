package se.homii.api;

import se.homii.api.models.Asset;
import se.homii.model.Image;

import java.io.IOException;

public interface ImageCreator {

  Image renderFrom(Asset asset, String userId)
      throws IOException;

}
