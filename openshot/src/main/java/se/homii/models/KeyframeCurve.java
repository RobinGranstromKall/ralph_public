package se.homii.models;

public enum KeyframeCurve {

  BEZIER(0),
  LINEAR(1),
  CONSTANT(2);

  private final Integer value;

  KeyframeCurve(final Integer value) {
    this.value = value;
  }

  public Integer getValue() {

    return value;
  }
}
