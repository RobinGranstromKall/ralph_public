package se.homii.models.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
public class ClipResponse {

  @JsonProperty("url")
  private String url;
  @JsonProperty("file")
  private String file;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("position")
  private Double position;
  @JsonProperty("start")
  private Double start;
  @JsonProperty("end")
  private Double end;
  @JsonProperty("layer")
  private Integer layer;
  @JsonProperty("actions")
  private List<String> actions = null;
  @JsonProperty("project")
  private String project;
  @JsonProperty("json")
  @JsonRawValue
  private ObjectNode json;
  @JsonProperty("date_created")
  private String dateCreated;
  @JsonProperty("date_updated")
  private String dateUpdated;

}