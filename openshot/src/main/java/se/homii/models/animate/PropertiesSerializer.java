package se.homii.models.animate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.CaseFormat;
import se.homii.models.animate.property.Properties;
import se.homii.models.animate.property.Property;

import java.io.IOException;

public class PropertiesSerializer extends StdSerializer<Properties> {

  public PropertiesSerializer() {

    this(null);
  }

  public PropertiesSerializer(Class<Properties> t) {

    super(t);
  }

  @Override
  public void serialize(Properties properties, JsonGenerator jsonGenerator,
                        SerializerProvider serializerProvider)
      throws IOException {

    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("scale", Properties.SCALE);
    jsonGenerator.writeNumberField("gravity", Properties.GRAVITY);
    for (Property property : properties.getProperties()) {
      jsonGenerator.writeObjectField(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, property.getClass().getSimpleName()), property);
    }

    jsonGenerator.writeEndObject();
  }
}
