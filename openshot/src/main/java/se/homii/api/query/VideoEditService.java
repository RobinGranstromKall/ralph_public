package se.homii.api.query;

import com.fasterxml.jackson.core.JsonProcessingException;

import se.homii.api.data.Asset;

public interface VideoEditService {

  void renderVideo(Asset asset)
      throws JsonProcessingException;
}
