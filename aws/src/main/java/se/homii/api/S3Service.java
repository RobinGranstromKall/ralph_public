package se.homii.api;

import java.io.File;

public interface S3Service {

  void uploadFileForUser(String userId, File file);
  void deleteFilesForUser(String userId);
  String resolveS3Url(String userId, String fileName);

}
