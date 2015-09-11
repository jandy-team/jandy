package io.jandy.util;

/**
 * @author JCooky
 * @since 2015-09-03
 */
public class SmallTime {
  public long value;
  public String unit;

  public static SmallTime format(long value) {
    boolean minus = value < 0;
    SmallTime t = new SmallTime();

    value = Math.abs(value);
    int loop = 0;
    while (value >= 1000) {
      value *= 0.001;
      loop++;
    }

    switch (loop) {
      case 0:
        t.unit = "ns";
        break;
      case 1:
        t.unit = "µs";
        break;
      case 2:
        t.unit = "ms";
        break;
      case 3:
        t.unit = "s";
        break;
      default:
        throw new IllegalArgumentException();
    }
    t.value = minus ? -value : value;

    return t;
  }

  public String toString() {
    return String.valueOf(value) + unit
        ;
  }

  public long getValue() {
    return value;
  }

  public String getUnit() {
    return unit;
  }
}
