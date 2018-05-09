package se.homii.api.query;

import java.net.URL;

public interface VideoEditWebhookService {

  void setStatusToFinished();
  void appendVideoUrl();
  URL getVideoUrl(String facebookUserId);
}
