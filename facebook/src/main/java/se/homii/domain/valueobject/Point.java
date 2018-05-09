package se.homii.domain.valueobject;

public enum Point {
  LIKE(1),
  COMMENT(4),
  POST(6),
  WITH_TAG(8),
  MESSAGE_TAG(8);

  private final int value;

  Point(int value) {

    this.value = value;
  }

  public int getValue() {

    return value;
  }
}
