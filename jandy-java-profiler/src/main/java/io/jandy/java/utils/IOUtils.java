package io.jandy.java.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class IOUtils {
  public static void writeUTF(DataOutput o, String s) throws IOException {
    if (s == null)
      o.writeBoolean(true);
    else {
      o.writeBoolean(false);
      o.writeUTF(s);
    }
  }

  public static String readUTF(DataInput i) throws IOException {
    return i.readBoolean() ? null : i.readUTF();
  }
}
