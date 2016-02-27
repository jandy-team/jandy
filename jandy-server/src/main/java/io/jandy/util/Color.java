package io.jandy.util;

public class Color {
  public static final Color FAILURE = new Color("Failure", 0x44, 0xcc, 0x11);
  public static final Color SUCCESS = new Color("Success", 0xe0, 0x5d, 0x44);

  public static final Color ALICE_BLUE = new Color("AliceBlue", 0xf0, 0xf8, 0xff);
  public static final Color ANTIQUE_WHITE = new Color("AntiqueWhite", 0xfa, 0xeb, 0xd7);
  public static final Color AQUA = new Color("Aqua", 0x0, 0xff, 0xff);
  public static final Color AQUAMARINE = new Color("Aquamarine", 0x7f, 0xff, 0xd4);
  public static final Color AZURE = new Color("Azure", 0xf0, 0xff, 0xff);
  public static final Color BEIGE = new Color("Beige", 0xf5, 0xf5, 0xdc);
  public static final Color BISQUE = new Color("Bisque", 0xff, 0xe4, 0xc4);
  public static final Color BLACK = new Color("Black", 0x0, 0x0, 0x0);
  public static final Color BLANCHED_ALMOND = new Color("BlanchedAlmond", 0xff, 0xeb, 0xcd);
  public static final Color BLUE = new Color("Blue", 0x0, 0x0, 0xff);
  public static final Color BLUE_VIOLET = new Color("BlueViolet", 0x8a, 0x2b, 0xe2);
  public static final Color BROWN = new Color("Brown", 0xa5, 0x2a, 0x2a);
  public static final Color BURLY_WOOD = new Color("BurlyWood", 0xde, 0xb8, 0x87);
  public static final Color CADET_BLUE = new Color("CadetBlue", 0x5f, 0x9e, 0xa0);
  public static final Color CHARTREUSE = new Color("Chartreuse", 0x7f, 0xff, 0x0);
  public static final Color CHOCOLATE = new Color("Chocolate", 0xd2, 0x69, 0x1e);
  public static final Color CORAL = new Color("Coral", 0xff, 0x7f, 0x50);
  public static final Color CORNFLOWER_BLUE = new Color("CornflowerBlue", 0x64, 0x95, 0xed);
  public static final Color CORNSILK = new Color("Cornsilk", 0xff, 0xf8, 0xdc);
  public static final Color CRIMSON = new Color("Crimson", 0xdc, 0x14, 0x3c);
  public static final Color CYAN = new Color("Cyan", 0x0, 0xff, 0xff);
  public static final Color DARK_BLUE = new Color("DarkBlue", 0x0, 0x0, 0x8b);
  public static final Color DARK_CYAN = new Color("DarkCyan", 0x0, 0x8b, 0x8b);
  public static final Color DARK_GOLDEN_ROD = new Color("DarkGoldenRod", 0xb8, 0x86, 0xb);
  public static final Color DARK_GRAY = new Color("DarkGray", 0xa9, 0xa9, 0xa9);
  public static final Color DARK_GREEN = new Color("DarkGreen", 0x0, 0x64, 0x0);
  public static final Color DARK_KHAKI = new Color("DarkKhaki", 0xbd, 0xb7, 0x6b);
  public static final Color DARK_MAGENTA = new Color("DarkMagenta", 0x8b, 0x0, 0x8b);
  public static final Color DARK_OLIVE_GREEN = new Color("DarkOliveGreen", 0x55, 0x6b, 0x2f);
  public static final Color DARK_ORANGE = new Color("DarkOrange", 0xff, 0x8c, 0x0);
  public static final Color DARK_ORCHID = new Color("DarkOrchid", 0x99, 0x32, 0xcc);
  public static final Color DARK_RED = new Color("DarkRed", 0x8b, 0x0, 0x0);
  public static final Color DARK_SALMON = new Color("DarkSalmon", 0xe9, 0x96, 0x7a);
  public static final Color DARK_SEA_GREEN = new Color("DarkSeaGreen", 0x8f, 0xbc, 0x8f);
  public static final Color DARK_SLATE_BLUE = new Color("DarkSlateBlue", 0x48, 0x3d, 0x8b);
  public static final Color DARK_SLATE_GRAY = new Color("DarkSlateGray", 0x2f, 0x4f, 0x4f);
  public static final Color DARK_TURQUOISE = new Color("DarkTurquoise", 0x0, 0xce, 0xd1);
  public static final Color DARK_VIOLET = new Color("DarkViolet", 0x94, 0x0, 0xd3);
  public static final Color DEEP_PINK = new Color("DeepPink", 0xff, 0x14, 0x93);
  public static final Color DEEP_SKY_BLUE = new Color("DeepSkyBlue", 0x0, 0xbf, 0xff);
  public static final Color DIM_GRAY = new Color("DimGray", 0x69, 0x69, 0x69);
  public static final Color DODGER_BLUE = new Color("DodgerBlue", 0x1e, 0x90, 0xff);
  public static final Color FIRE_BRICK = new Color("FireBrick", 0xb2, 0x22, 0x22);
  public static final Color FLORAL_WHITE = new Color("FloralWhite", 0xff, 0xfa, 0xf0);
  public static final Color FOREST_GREEN = new Color("ForestGreen", 0x22, 0x8b, 0x22);
  public static final Color FUCHSIA = new Color("Fuchsia", 0xff, 0x0, 0xff);
  public static final Color GAINSBORO = new Color("Gainsboro", 0xdc, 0xdc, 0xdc);
  public static final Color GHOST_WHITE = new Color("GhostWhite", 0xf8, 0xf8, 0xff);
  public static final Color GOLD = new Color("Gold", 0xff, 0xd7, 0x0);
  public static final Color GOLDEN_ROD = new Color("GoldenRod", 0xda, 0xa5, 0x20);
  public static final Color GRAY = new Color("Gray", 0x80, 0x80, 0x80);
  public static final Color GREEN = new Color("Green", 0x0, 0x80, 0x0);
  public static final Color GREEN_YELLOW = new Color("GreenYellow", 0xad, 0xff, 0x2f);
  public static final Color HONEY_DEW = new Color("HoneyDew", 0xf0, 0xff, 0xf0);
  public static final Color HOT_PINK = new Color("HotPink", 0xff, 0x69, 0xb4);
  public static final Color INDIAN_RED = new Color("IndianRed", 0xcd, 0x5c, 0x5c);
  public static final Color INDIGO = new Color("Indigo", 0x4b, 0x0, 0x82);
  public static final Color IVORY = new Color("Ivory", 0xff, 0xff, 0xf0);
  public static final Color KHAKI = new Color("Khaki", 0xf0, 0xe6, 0x8c);
  public static final Color LAVENDER = new Color("Lavender", 0xe6, 0xe6, 0xfa);
  public static final Color LAVENDER_BLUSH = new Color("LavenderBlush", 0xff, 0xf0, 0xf5);
  public static final Color LAWN_GREEN = new Color("LawnGreen", 0x7c, 0xfc, 0x0);
  public static final Color LEMON_CHIFFON = new Color("LemonChiffon", 0xff, 0xfa, 0xcd);
  public static final Color LIGHT_BLUE = new Color("LightBlue", 0xad, 0xd8, 0xe6);
  public static final Color LIGHT_CORAL = new Color("LightCoral", 0xf0, 0x80, 0x80);
  public static final Color LIGHT_CYAN = new Color("LightCyan", 0xe0, 0xff, 0xff);
  public static final Color LIGHT_GOLDEN_ROD_YELLOW = new Color("LightGoldenRodYellow", 0xfa, 0xfa, 0xd2);
  public static final Color LIGHT_GRAY = new Color("LightGray", 0xd3, 0xd3, 0xd3);
  public static final Color LIGHT_GREEN = new Color("LightGreen", 0x90, 0xee, 0x90);
  public static final Color LIGHT_PINK = new Color("LightPink", 0xff, 0xb6, 0xc1);
  public static final Color LIGHT_SALMON = new Color("LightSalmon", 0xff, 0xa0, 0x7a);
  public static final Color LIGHT_SEA_GREEN = new Color("LightSeaGreen", 0x20, 0xb2, 0xaa);
  public static final Color LIGHT_SKY_BLUE = new Color("LightSkyBlue", 0x87, 0xce, 0xfa);
  public static final Color LIGHT_SLATE_GRAY = new Color("LightSlateGray", 0x77, 0x88, 0x99);
  public static final Color LIGHT_STEEL_BLUE = new Color("LightSteelBlue", 0xb0, 0xc4, 0xde);
  public static final Color LIGHT_YELLOW = new Color("LightYellow", 0xff, 0xff, 0xe0);
  public static final Color LIME = new Color("Lime", 0x0, 0xff, 0x0);
  public static final Color LIME_GREEN = new Color("LimeGreen", 0x32, 0xcd, 0x32);
  public static final Color LINEN = new Color("Linen", 0xfa, 0xf0, 0xe6);
  public static final Color MAGENTA = new Color("Magenta", 0xff, 0x0, 0xff);
  public static final Color MAROON = new Color("Maroon", 0x80, 0x0, 0x0);
  public static final Color MEDIUM_AQUA_MARINE = new Color("MediumAquaMarine", 0x66, 0xcd, 0xaa);
  public static final Color MEDIUM_BLUE = new Color("MediumBlue", 0x0, 0x0, 0xcd);
  public static final Color MEDIUM_ORCHID = new Color("MediumOrchid", 0xba, 0x55, 0xd3);
  public static final Color MEDIUM_PURPLE = new Color("MediumPurple", 0x93, 0x70, 0xdb);
  public static final Color MEDIUM_SEA_GREEN = new Color("MediumSeaGreen", 0x3c, 0xb3, 0x71);
  public static final Color MEDIUM_SLATE_BLUE = new Color("MediumSlateBlue", 0x7b, 0x68, 0xee);
  public static final Color MEDIUM_SPRING_GREEN = new Color("MediumSpringGreen", 0x0, 0xfa, 0x9a);
  public static final Color MEDIUM_TURQUOISE = new Color("MediumTurquoise", 0x48, 0xd1, 0xcc);
  public static final Color MEDIUM_VIOLET_RED = new Color("MediumVioletRed", 0xc7, 0x15, 0x85);
  public static final Color MIDNIGHT_BLUE = new Color("MidnightBlue", 0x19, 0x19, 0x70);
  public static final Color MINT_CREAM = new Color("MintCream", 0xf5, 0xff, 0xfa);
  public static final Color MISTY_ROSE = new Color("MistyRose", 0xff, 0xe4, 0xe1);
  public static final Color MOCCASIN = new Color("Moccasin", 0xff, 0xe4, 0xb5);
  public static final Color NAVAJO_WHITE = new Color("NavajoWhite", 0xff, 0xde, 0xad);
  public static final Color NAVY = new Color("Navy", 0x0, 0x0, 0x80);
  public static final Color OLD_LACE = new Color("OldLace", 0xfd, 0xf5, 0xe6);
  public static final Color OLIVE = new Color("Olive", 0x80, 0x80, 0x0);
  public static final Color OLIVE_DRAB = new Color("OliveDrab", 0x6b, 0x8e, 0x23);
  public static final Color ORANGE = new Color("Orange", 0xff, 0xa5, 0x0);
  public static final Color ORANGE_RED = new Color("OrangeRed", 0xff, 0x45, 0x0);
  public static final Color ORCHID = new Color("Orchid", 0xda, 0x70, 0xd6);
  public static final Color PALE_GOLDEN_ROD = new Color("PaleGoldenRod", 0xee, 0xe8, 0xaa);
  public static final Color PALE_GREEN = new Color("PaleGreen", 0x98, 0xfb, 0x98);
  public static final Color PALE_TURQUOISE = new Color("PaleTurquoise", 0xaf, 0xee, 0xee);
  public static final Color PALE_VIOLET_RED = new Color("PaleVioletRed", 0xdb, 0x70, 0x93);
  public static final Color PAPAYA_WHIP = new Color("PapayaWhip", 0xff, 0xef, 0xd5);
  public static final Color PEACH_PUFF = new Color("PeachPuff", 0xff, 0xda, 0xb9);
  public static final Color PERU = new Color("Peru", 0xcd, 0x85, 0x3f);
  public static final Color PINK = new Color("Pink", 0xff, 0xc0, 0xcb);
  public static final Color PLUM = new Color("Plum", 0xdd, 0xa0, 0xdd);
  public static final Color POWDER_BLUE = new Color("PowderBlue", 0xb0, 0xe0, 0xe6);
  public static final Color PURPLE = new Color("Purple", 0x80, 0x0, 0x80);
  public static final Color RED = new Color("Red", 0xff, 0x0, 0x0);
  public static final Color ROSY_BROWN = new Color("RosyBrown", 0xbc, 0x8f, 0x8f);
  public static final Color ROYAL_BLUE = new Color("RoyalBlue", 0x41, 0x69, 0xe1);
  public static final Color SADDLE_BROWN = new Color("SaddleBrown", 0x8b, 0x45, 0x13);
  public static final Color SALMON = new Color("Salmon", 0xfa, 0x80, 0x72);
  public static final Color SANDY_BROWN = new Color("SandyBrown", 0xf4, 0xa4, 0x60);
  public static final Color SEA_GREEN = new Color("SeaGreen", 0x2e, 0x8b, 0x57);
  public static final Color SEA_SHELL = new Color("SeaShell", 0xff, 0xf5, 0xee);
  public static final Color SIENNA = new Color("Sienna", 0xa0, 0x52, 0x2d);
  public static final Color SILVER = new Color("Silver", 0xc0, 0xc0, 0xc0);
  public static final Color SKY_BLUE = new Color("SkyBlue", 0x87, 0xce, 0xeb);
  public static final Color SLATE_BLUE = new Color("SlateBlue", 0x6a, 0x5a, 0xcd);
  public static final Color SLATE_GRAY = new Color("SlateGray", 0x70, 0x80, 0x90);
  public static final Color SNOW = new Color("Snow", 0xff, 0xfa, 0xfa);
  public static final Color SPRING_GREEN = new Color("SpringGreen", 0x0, 0xff, 0x7f);
  public static final Color STEEL_BLUE = new Color("SteelBlue", 0x46, 0x82, 0xb4);
  public static final Color TAN = new Color("Tan", 0xd2, 0xb4, 0x8c);
  public static final Color TEAL = new Color("Teal", 0x0, 0x80, 0x80);
  public static final Color THISTLE = new Color("Thistle", 0xd8, 0xbf, 0xd8);
  public static final Color TOMATO = new Color("Tomato", 0xff, 0x63, 0x47);
  public static final Color TURQUOISE = new Color("Turquoise", 0x40, 0xe0, 0xd0);
  public static final Color VIOLET = new Color("Violet", 0xee, 0x82, 0xee);
  public static final Color WHEAT = new Color("Wheat", 0xf5, 0xde, 0xb3);
  public static final Color WHITE = new Color("White", 0xff, 0xff, 0xff);
  public static final Color WHITE_SMOKE = new Color("WhiteSmoke", 0xf5, 0xf5, 0xf5);
  public static final Color YELLOW = new Color("Yellow", 0xff, 0xff, 0x0);
  public static final Color YELLOW_GREEN = new Color("YellowGreen", 0x9a, 0xcd, 0x32);

  public int r, g, b;
  public String name;

  public Color(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public Color(String name, int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.name = name;
  }

  public int computeMSE(int pixR, int pixG, int pixB) {
    return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
        * (pixB - b)) / 3);
  }

  public int getR() {
    return r;
  }

  public int getG() {
    return g;
  }

  public int getB() {
    return b;
  }

  public String getCssValue() {
    return "rgb("+r+", "+g+", "+b+")";
  }

  public String getName() {
    return name;
  }
}