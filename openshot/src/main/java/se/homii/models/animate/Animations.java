package se.homii.models.animate;

import se.homii.models.animate.property.*;

import java.util.ArrayList;
import java.util.List;

import static se.homii.models.KeyframeCurve.LINEAR;

public class Animations {

  public static final Double PAUSE_TIME = 0.5;
  public static final Double TRANSITION_TIME = 1.0;
  private static final Double Y_CENTER = 0.4;

  public LocationY moveY(Double distance, Double start) {

    List<Point> points = new ArrayList<>();
    points.add(buildPoint(start, Y_CENTER));
    points.add(buildPoint(start + PAUSE_TIME, distance + Y_CENTER));

    return LocationY.builder()
        .points(points)
        .build();
  }

  public List<Property> scale(Double scaleX, Double scaleY) {

    ScaleX scaleXProperty = ScaleX.builder()
        .point((buildPoint(0.0, scaleX)))
        .build();
    ScaleY scaleYProperty = ScaleY.builder()
        .point(buildPoint(0.0, scaleY))
        .build();

    List<Property> properties = new ArrayList<>();
    properties.add(scaleXProperty);
    properties.add(scaleYProperty);

    return properties;
  }

  public Alpha transition(Double end) {

    return fadeInOut(end);
  }

  public Alpha commentTransition(Double end) {

    return lateFadeInOut(end);
  }

  private Alpha lateFadeInOut(Double end) {

    List<Point> points = new ArrayList<>();

    points.add(buildPoint(0.0, 0.0));
    points.add(buildPoint(TRANSITION_TIME - 0.034, 0.0));
    points.add(buildPoint(TRANSITION_TIME, 1.0));
    points.add(buildPoint((end - TRANSITION_TIME), 1.0));
    points.add(buildPoint(end, 0.0));

    return Alpha.builder()
        .points(points)
        .build();
  }

  private Alpha fadeInOut(Double end) {

    List<Point> points = new ArrayList<>();

    points.add(buildPoint(0.0, 0.0));
    points.add(buildPoint(TRANSITION_TIME, 1.0));
    points.add(buildPoint((end - TRANSITION_TIME), 1.0));
    points.add(buildPoint(end, 0.0));

    return Alpha.builder()
        .points(points)
        .build();
  }

  public LocationY centerY() {

    return LocationY.builder()
        .point(buildPoint(0.0, Y_CENTER))
        .build();
  }

  private LocationX panFromLeft() {

    List<Point> points = new ArrayList<>();

    points.add(buildPoint(0.0, -1.0));
    points.add(buildPoint(TRANSITION_TIME, 0.0));

    return LocationX.builder()
        .points(points)
        .build();
  }

  private Point buildPoint(Double position, Double value) {

    return Point.builder()
        .coordinates(Coordinates.builder()
            .frame(translateSecToFrame(position))
            .value(value)
            .build())
        .interpolation(LINEAR.getValue())
        .build();
  }

  private Integer translateSecToFrame(Double positionInSec) {

    Double result = positionInSec * 30;

    return (int) (result + 0.5); // TODO is this right? QUICK MATHS!?!?
  }
}
