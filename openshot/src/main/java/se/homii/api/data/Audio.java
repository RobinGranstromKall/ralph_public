package se.homii.api.data;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class Audio {

  private final String fileUrl;
  private final Double fileLength;

}
