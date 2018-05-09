package se.homii.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.List;

@Data
public class FileResponse {

  @JsonProperty("url")
  private String url;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("media")
  private String media;
  @JsonProperty("project")
  private String project;
  @JsonProperty("actions")
  private List<String> actions;
  @JsonProperty("json")
  @JsonRawValue
  private ObjectNode json;
  @JsonProperty("date_created")
  private String dateCreated;
  @JsonProperty("date_updated")
  private String dateUpdated;

}