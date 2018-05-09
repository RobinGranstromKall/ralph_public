package se.homii.google.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Translation {

  private final String text;
}
