package se.homii.api;

import java.io.File;

public interface S3Service {

  String uploadFileForUser(String userId, File file);
  void deleteFilesForUser(String userId);
}
