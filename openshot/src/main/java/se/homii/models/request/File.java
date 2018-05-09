package se.homii.models.request;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {

  private java.io.File media; // TODO file or what??
  private String project;
  @JsonRawValue
  private String json;

}
