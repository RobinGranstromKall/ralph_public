package se.homii.models.request;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Clip {

  public String file;
  public Double position;
  public Integer start;
  public Double end;
  public Integer layer;
  public String project;
  @JsonRawValue
  public String json;

}