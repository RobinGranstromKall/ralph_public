package se.homii.models.request;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Project {

  private String name;
  private final Integer width = 1280;
  private final Integer height = 720;
  private final Integer fps_num = 30;
  private final Integer fps_den = 1;
  private final Integer sample_rate = 44100;
  private final Integer channels = 2;
  private final Integer channel_layout = 3;
  @JsonRawValue
  private String json;
}