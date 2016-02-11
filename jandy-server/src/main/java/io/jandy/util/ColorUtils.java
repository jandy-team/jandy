package io.jandy.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Java Code to get a color name from rgb/hex value/awt color
 * <p/>
 * The part of looking up a color name from the rgb values is edited from
 * https://gist.github.com/nightlark/6482130#file-gistfile1-java (that has some errors) by Ryan Mast (nightlark)
 *
 * @author Xiaoxiao Li
 */
public class ColorUtils {

  private static class RGB {
    public RGB() {

    }

    public RGB(double r, double g, double b) {
      this.r = r;
      this.g = g;
      this.b = b;
    }

    public double r, g, b;
  }

  private static class HSV {
    public HSV() {

    }

    public HSV(double h, double s, double v) {
      this.h = h;
      this.s = s;
      this.v = v;
    }

    public double h, s, v;
  }

  private static ArrayList<Color> COLOR_NAMES = new ArrayList<Color>();

  /**
   * Initialize the color list that we have.
   */
  static {
    COLOR_NAMES.add(Color.ALICE_BLUE);
    COLOR_NAMES.add(Color.ANTIQUE_WHITE);
    COLOR_NAMES.add(Color.AQUA);
    COLOR_NAMES.add(Color.AQUAMARINE);
    COLOR_NAMES.add(Color.AZURE);
    COLOR_NAMES.add(Color.BEIGE);
    COLOR_NAMES.add(Color.BISQUE);
    COLOR_NAMES.add(Color.BLACK);
    COLOR_NAMES.add(Color.BLANCHED_ALMOND);
    COLOR_NAMES.add(Color.BLUE);
    COLOR_NAMES.add(Color.BLUE_VIOLET);
    COLOR_NAMES.add(Color.BROWN);
    COLOR_NAMES.add(Color.BURLY_WOOD);
    COLOR_NAMES.add(Color.CADET_BLUE);
    COLOR_NAMES.add(Color.CHARTREUSE);
    COLOR_NAMES.add(Color.CHOCOLATE);
    COLOR_NAMES.add(Color.CORAL);
    COLOR_NAMES.add(Color.CORNFLOWER_BLUE);
    COLOR_NAMES.add(Color.CORNSILK);
    COLOR_NAMES.add(Color.CRIMSON);
    COLOR_NAMES.add(Color.CYAN);
    COLOR_NAMES.add(Color.DARK_BLUE);
    COLOR_NAMES.add(Color.DARK_CYAN);
    COLOR_NAMES.add(Color.DARK_GOLDEN_ROD);
    COLOR_NAMES.add(Color.DARK_GRAY);
    COLOR_NAMES.add(Color.DARK_GREEN);
    COLOR_NAMES.add(Color.DARK_KHAKI);
    COLOR_NAMES.add(Color.DARK_MAGENTA);
    COLOR_NAMES.add(Color.DARK_OLIVE_GREEN);
    COLOR_NAMES.add(Color.DARK_ORANGE);
    COLOR_NAMES.add(Color.DARK_ORCHID);
    COLOR_NAMES.add(Color.DARK_RED);
    COLOR_NAMES.add(Color.DARK_SALMON);
    COLOR_NAMES.add(Color.DARK_SEA_GREEN);
    COLOR_NAMES.add(Color.DARK_SLATE_BLUE);
    COLOR_NAMES.add(Color.DARK_SLATE_GRAY);
    COLOR_NAMES.add(Color.DARK_TURQUOISE);
    COLOR_NAMES.add(Color.DARK_VIOLET);
    COLOR_NAMES.add(Color.DEEP_PINK);
    COLOR_NAMES.add(Color.DEEP_SKY_BLUE);
    COLOR_NAMES.add(Color.DIM_GRAY);
    COLOR_NAMES.add(Color.DODGER_BLUE);
    COLOR_NAMES.add(Color.FIRE_BRICK);
    COLOR_NAMES.add(Color.FLORAL_WHITE);
    COLOR_NAMES.add(Color.FOREST_GREEN);
    COLOR_NAMES.add(Color.FUCHSIA);
    COLOR_NAMES.add(Color.GAINSBORO);
    COLOR_NAMES.add(Color.GHOST_WHITE);
    COLOR_NAMES.add(Color.GOLD);
    COLOR_NAMES.add(Color.GOLDEN_ROD);
    COLOR_NAMES.add(Color.GRAY);
    COLOR_NAMES.add(Color.GREEN);
    COLOR_NAMES.add(Color.GREEN_YELLOW);
    COLOR_NAMES.add(Color.HONEY_DEW);
    COLOR_NAMES.add(Color.HOT_PINK);
    COLOR_NAMES.add(Color.INDIAN_RED);
    COLOR_NAMES.add(Color.INDIGO);
    COLOR_NAMES.add(Color.IVORY);
    COLOR_NAMES.add(Color.KHAKI);
    COLOR_NAMES.add(Color.LAVENDER);
    COLOR_NAMES.add(Color.LAVENDER_BLUSH);
    COLOR_NAMES.add(Color.LAWN_GREEN);
    COLOR_NAMES.add(Color.LEMON_CHIFFON);
    COLOR_NAMES.add(Color.LIGHT_BLUE);
    COLOR_NAMES.add(Color.LIGHT_CORAL);
    COLOR_NAMES.add(Color.LIGHT_CYAN);
    COLOR_NAMES.add(Color.LIGHT_GOLDEN_ROD_YELLOW);
    COLOR_NAMES.add(Color.LIGHT_GRAY);
    COLOR_NAMES.add(Color.LIGHT_GREEN);
    COLOR_NAMES.add(Color.LIGHT_PINK);
    COLOR_NAMES.add(Color.LIGHT_SALMON);
    COLOR_NAMES.add(Color.LIGHT_SEA_GREEN);
    COLOR_NAMES.add(Color.LIGHT_SKY_BLUE);
    COLOR_NAMES.add(Color.LIGHT_SLATE_GRAY);
    COLOR_NAMES.add(Color.LIGHT_STEEL_BLUE);
    COLOR_NAMES.add(Color.LIGHT_YELLOW);
    COLOR_NAMES.add(Color.LIME);
    COLOR_NAMES.add(Color.LIME_GREEN);
    COLOR_NAMES.add(Color.LINEN);
    COLOR_NAMES.add(Color.MAGENTA);
    COLOR_NAMES.add(Color.MAROON);
    COLOR_NAMES.add(Color.MEDIUM_AQUA_MARINE);
    COLOR_NAMES.add(Color.MEDIUM_BLUE);
    COLOR_NAMES.add(Color.MEDIUM_ORCHID);
    COLOR_NAMES.add(Color.MEDIUM_PURPLE);
    COLOR_NAMES.add(Color.MEDIUM_SEA_GREEN);
    COLOR_NAMES.add(Color.MEDIUM_SLATE_BLUE);
    COLOR_NAMES.add(Color.MEDIUM_SPRING_GREEN);
    COLOR_NAMES.add(Color.MEDIUM_TURQUOISE);
    COLOR_NAMES.add(Color.MEDIUM_VIOLET_RED);
    COLOR_NAMES.add(Color.MIDNIGHT_BLUE);
    COLOR_NAMES.add(Color.MINT_CREAM);
    COLOR_NAMES.add(Color.MISTY_ROSE);
    COLOR_NAMES.add(Color.MOCCASIN);
    COLOR_NAMES.add(Color.NAVAJO_WHITE);
    COLOR_NAMES.add(Color.NAVY);
    COLOR_NAMES.add(Color.OLD_LACE);
    COLOR_NAMES.add(Color.OLIVE);
    COLOR_NAMES.add(Color.OLIVE_DRAB);
    COLOR_NAMES.add(Color.ORANGE);
    COLOR_NAMES.add(Color.ORANGE_RED);
    COLOR_NAMES.add(Color.ORCHID);
    COLOR_NAMES.add(Color.PALE_GOLDEN_ROD);
    COLOR_NAMES.add(Color.PALE_GREEN);
    COLOR_NAMES.add(Color.PALE_TURQUOISE);
    COLOR_NAMES.add(Color.PALE_VIOLET_RED);
    COLOR_NAMES.add(Color.PAPAYA_WHIP);
    COLOR_NAMES.add(Color.PEACH_PUFF);
    COLOR_NAMES.add(Color.PERU);
    COLOR_NAMES.add(Color.PINK);
    COLOR_NAMES.add(Color.PLUM);
    COLOR_NAMES.add(Color.POWDER_BLUE);
    COLOR_NAMES.add(Color.PURPLE);
    COLOR_NAMES.add(Color.RED);
    COLOR_NAMES.add(Color.ROSY_BROWN);
    COLOR_NAMES.add(Color.ROYAL_BLUE);
    COLOR_NAMES.add(Color.SADDLE_BROWN);
    COLOR_NAMES.add(Color.SALMON);
    COLOR_NAMES.add(Color.SANDY_BROWN);
    COLOR_NAMES.add(Color.SEA_GREEN);
    COLOR_NAMES.add(Color.SEA_SHELL);
    COLOR_NAMES.add(Color.SIENNA);
    COLOR_NAMES.add(Color.SILVER);
    COLOR_NAMES.add(Color.SKY_BLUE);
    COLOR_NAMES.add(Color.SLATE_BLUE);
    COLOR_NAMES.add(Color.SLATE_GRAY);
    COLOR_NAMES.add(Color.SNOW);
    COLOR_NAMES.add(Color.SPRING_GREEN);
    COLOR_NAMES.add(Color.STEEL_BLUE);
    COLOR_NAMES.add(Color.TAN);
    COLOR_NAMES.add(Color.TEAL);
    COLOR_NAMES.add(Color.THISTLE);
    COLOR_NAMES.add(Color.TOMATO);
    COLOR_NAMES.add(Color.TURQUOISE);
    COLOR_NAMES.add(Color.VIOLET);
    COLOR_NAMES.add(Color.WHEAT);
    COLOR_NAMES.add(Color.WHITE);
    COLOR_NAMES.add(Color.WHITE_SMOKE);
    COLOR_NAMES.add(Color.YELLOW);
    COLOR_NAMES.add(Color.YELLOW_GREEN);
  }

  public static String getRandomColor() {
    Color color = COLOR_NAMES.get(RandomUtils.nextInt(0, COLOR_NAMES.size()));
    return String.format("rgb(%d, %d, %d)", color.r, color.g, color.b);
  }

  public static List<String> getRandomColors(int count) {
    if (count > COLOR_NAMES.size())
      throw new IllegalArgumentException("count is more than list's size: " + COLOR_NAMES.size());

    List<String> colors = new ArrayList<>(count);
    for (int i = 0; i < count; ++i) {
      String color = getRandomColor();
      if (i == 0 || !color.equals(colors.get(i - 1))) {
        colors.add(color);
      } else {
        --i;
      }
    }

    return colors;
  }

  public static Color interpolate(Color start, Color end, double interp) {
    double inv = 1 - interp;
    RGB rgb0 = new RGB(start.r, start.g, start.b),
        rgb1 = new RGB(end.r, end.g, end.b),
        rgb;

    HSV hsv0 = rgb2hsv(rgb0), hsv1 = rgb2hsv(rgb1), hsv = new HSV();

    hsv.h = hsv0.h * interp + hsv1.h * inv;
    hsv.s = hsv0.s * interp + hsv1.s * inv;
    hsv.v = hsv0.v * interp + hsv1.v * inv;

    rgb = hsv2rgb(hsv);

    return new Color((int)rgb.r, (int)rgb.g, (int)rgb.b);
  }

  public static HSV rgb2hsv(RGB in) {
    HSV out = new HSV();
    double min, max, delta;

    min = in.r < in.g ? in.r : in.g;
    min = min < in.b ? min : in.b;

    max = in.r > in.g ? in.r : in.g;
    max = max > in.b ? max : in.b;

    out.v = max;                                // v
    delta = max - min;
    if (delta < 0.00001) {
      out.s = 0;
      out.h = 0; // undefined, maybe nan?
      return out;
    }
    if (max > 0.0) { // NOTE: if Max is == 0, this divide would cause a crash
      out.s = (delta / max);                  // s
    } else {
      // if max is 0, then r = g = b = 0
      // s = 0, v is undefined
      out.s = 0.0;
      out.h = Double.NaN;                            // its now undefined
      return out;
    }
    if (in.r >= max)                           // > is bogus, just keeps compilor happy
      out.h = (in.g - in.b) / delta;        // between yellow & magenta
    else if (in.g >= max)
      out.h = 2.0 + (in.b - in.r) / delta;  // between cyan & yellow
    else
      out.h = 4.0 + (in.r - in.g) / delta;  // between magenta & cyan

    out.h *= 60.0;                              // degrees

    if (out.h < 0.0)
      out.h += 360.0;

    return out;
  }


  public static RGB hsv2rgb(HSV in) {
    double hh, p, q, t, ff;
    int i;
    RGB out = new RGB();

    if (in.s <= 0.0) {       // < is bogus, just shuts up warnings
      out.r = in.v;
      out.g = in.v;
      out.b = in.v;
      return out;
    }
    hh = in.h;
    if (hh >= 360.0) hh = 0.0;
    hh /= 60.0;
    i = (int) hh;
    ff = hh - i;
    p = in.v * (1.0 - in.s);
    q = in.v * (1.0 - (in.s * ff));
    t = in.v * (1.0 - (in.s * (1.0 - ff)));

    switch (i) {
      case 0:
        out.r = in.v;
        out.g = t;
        out.b = p;
        break;
      case 1:
        out.r = q;
        out.g = in.v;
        out.b = p;
        break;
      case 2:
        out.r = p;
        out.g = in.v;
        out.b = t;
        break;

      case 3:
        out.r = p;
        out.g = q;
        out.b = in.v;
        break;
      case 4:
        out.r = t;
        out.g = p;
        out.b = in.v;
        break;
      case 5:
      default:
        out.r = in.v;
        out.g = p;
        out.b = q;
        break;
    }
    return out;
  }

}