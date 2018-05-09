package se.homii;

import org.glassfish.jersey.logging.LoggingFeature;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class ClientHelper {

  private final Client client;

  private final String baseUrl = "http://52.50.10.43/";
  //private final String baseUrl = "http://p5323.mocklab.io/";


  private String projectUrl;

  public ClientHelper() {

    Logger logger = Logger.getLogger(getClass().getName());

    Feature feature = new LoggingFeature(logger, Level.INFO, null, null);

    this.client = ClientBuilder.newBuilder()
        .register(feature)
        .build();

  }

  public String getProjectUrl() {

    return projectUrl;
  }

  public void setProjectUrl(String projectUrl) {

    this.projectUrl = projectUrl;
  }


  // FIXME so the client builds more dynamically
  public Invocation.Builder buildClientWithHeader(String endpoint, Integer clipId) {

    String url;

    if (clipId == null) {

      url = String.format("%s%s", projectUrl, endpoint);
    } else {

      url = String.format("%sclips/%d/%s", baseUrl, clipId, endpoint);
    }


    return client.target(url)
        .request()
        .headers(getHeaders());
  }

  public Invocation.Builder buildClientWithHeader() {

    return client.target(String.format("%sprojects/", baseUrl))
        .request()
        .headers(getHeaders());
  }

  private MultivaluedMap<String, Object> getHeaders() {

    MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<>();
    myHeaders.add("Authorization", "Token token");
    myHeaders.add("Accept", "application/json");

    return myHeaders;
  }

  public Entity<Object> buildEntity(Object object) {

    return Entity.entity(object,
        MediaType.APPLICATION_JSON_TYPE); //TODO perhaps not _TYPE??
  }
}
