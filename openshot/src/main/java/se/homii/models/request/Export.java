package se.homii.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Export {

  @JsonProperty("export_type")
  public String exportType;
  @JsonProperty("video_format")
  public String videoFormat;
  @JsonProperty("video_codec")
  public String videoCodec;
  @JsonProperty("video_bitrate")
  public Integer videoBitrate;
  @JsonProperty("audio_codec")
  public String audioCodec;
  @JsonProperty("audio_bitrate")
  public Integer audioBitrate;
  @JsonProperty("start_frame")
  public Integer startFrame;
  @JsonProperty("end_frame")
  public Integer endFrame;
  public String project;
  public String webhook;
  @JsonRawValue
  public String json;
  public String status;

}