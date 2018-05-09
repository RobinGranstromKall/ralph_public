package se.homii;


import se.homii.models.response.ExportResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/webhook")
public class WebhookResource {

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void webhook(@BeanParam ExportResponse exportResponse) {

    // Webhook that is to be called when the rendering of the video is done or has failed

    if(exportResponse.status.equals("completed")) {
      System.out.println(
          "Render of http://52.50.10.43/exports/" + exportResponse.id + " is completed");
    } if (exportResponse.status.equals("failed")) {
      System.out.println(
          "Render of http://52.50.10.43/exports/" + exportResponse.id + " failed");
    } else {
      System.out.println(
          exportResponse.status + " http://52.50.10.43/exports/" + exportResponse.id);
    }
  }
}
