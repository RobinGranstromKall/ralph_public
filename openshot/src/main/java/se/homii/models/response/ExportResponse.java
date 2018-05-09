package se.homii.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import javax.ws.rs.FormParam;
import java.util.List;

@Data
public class ExportResponse {

  @FormParam("url")
  @JsonProperty("url")
  public String url;
  @FormParam("id")
  @JsonProperty("id")
  public Integer id;
  @FormParam("output")
  @JsonProperty("output")
  public String output;
  @FormParam("export_type")
  @JsonProperty("export_type")
  public String exportType;
  @FormParam("video_format")
  @JsonProperty("video_format")
  public String videoFormat;
  @FormParam("video_codec")
  @JsonProperty("video_codec")
  public String videoCodec;
  @FormParam("video_bitrate")
  @JsonProperty("video_bitrate")
  public Integer videoBitrate;
  @FormParam("audio_codec")
  @JsonProperty("audio_codec")
  public String audioCodec;
  @FormParam("audio_bitrate")
  @JsonProperty("audio_bitrate")
  public Integer audioBitrate;
  @FormParam("start_frame")
  @JsonProperty("start_frame")
  public Integer startFrame;
  @FormParam("end_frame")
  @JsonProperty("end_frame")
  public Integer endFrame;
  @FormParam("actions")
  @JsonProperty("actions")
  public List<String> actions;
  @FormParam("project")
  @JsonProperty("project")
  public String project;
  @FormParam("webhook")
  @JsonProperty("webhook")
  public String webhook;
  @FormParam("json")
  @JsonProperty("json")
  @JsonRawValue
  public ObjectNode json;
  @FormParam("progress")
  @JsonProperty("progress")
  public Double progress;
  @FormParam("status")
  @JsonProperty("status")
  public String status;
  @FormParam("date_created")
  @JsonProperty("date_created")
  public String dateCreated;
  @FormParam("date_updated")
  @JsonProperty("date_updated")
  public String dateUpdated;

}