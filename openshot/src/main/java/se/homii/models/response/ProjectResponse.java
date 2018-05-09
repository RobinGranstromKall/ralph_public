package se.homii.models.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
public class ProjectResponse {

  @JsonProperty("url")
  private String url;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("name")
  private String name;
  @JsonProperty("width")
  private Integer width;
  @JsonProperty("height")
  private Integer height;
  @JsonProperty("fps_num")
  private Integer fpsNum;
  @JsonProperty("fps_den")
  private Integer fpsDen;
  @JsonProperty("sample_rate")
  private Integer sampleRate;
  @JsonProperty("channels")
  private Integer channels;
  @JsonProperty("channel_layout")
  private Integer channelLayout;
  @JsonProperty("files")
  private List<Object> files;
  @JsonProperty("clips")
  private List<Object> clips;
  @JsonProperty("effects")
  private List<Object> effects;
  @JsonProperty("exports")
  private List<Object> exports;
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