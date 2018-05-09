package se.homii;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import se.homii.api.S3Service;

import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class S3ServiceImpl implements S3Service {

  private final String accessKey = "KEY";
  private final String secretAccessKey = "Secret";
  private final String BUCKET_NAME = "nameofbucket";
  private final String REGION = "region";

  private AmazonS3 s3;

  public S3ServiceImpl() {

    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
        accessKey,
        secretAccessKey);

    this.s3 = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(Regions.fromName(REGION))
        .build();
  }

  @Override
  public String uploadFileForUser(String userId, File file) {

    String bucket = BUCKET_NAME + "/" + userId;

    try {
      s3.putObject(new PutObjectRequest(bucket, file.getName(), file));
    } catch (AmazonClientException e) {
      e.printStackTrace();
    }

    return String.format("http://s3-eu-west-1.amazonaws.com/homii-ralph/%s/%s", userId, file.getName());
  }

  @Override
  public void deleteFilesForUser(String userId) {

    ObjectListing objectListing = s3.listObjects(BUCKET_NAME, userId);
    DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(BUCKET_NAME);

    List<KeyVersion> keys = new ArrayList<>();

    for (S3ObjectSummary keyName : objectListing.getObjectSummaries()) {
      keys.add(new KeyVersion(keyName.getKey()));
    }

    multiObjectDeleteRequest.setKeys(keys);

    try {
      s3.deleteObjects(multiObjectDeleteRequest);
    } catch (MultiObjectDeleteException e) {
      e.printStackTrace();
    }
  }
}
