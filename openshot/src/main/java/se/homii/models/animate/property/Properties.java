package se.homii.models.animate.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import se.homii.models.animate.PropertiesSerializer;
import se.homii.models.animate.property.Property;

import java.util.List;

@Data
@Builder
@JsonSerialize(using = PropertiesSerializer.class)
public class Properties {

  public final static Integer SCALE = 2;
  public final static Integer GRAVITY = 1;

  @Singular
  List<Property> properties;
}
