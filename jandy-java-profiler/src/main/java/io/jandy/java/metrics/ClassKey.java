package io.jandy.java.metrics;

import io.jandy.java.utils.IOUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class ClassKey implements Externalizable {
  private String name;
  private String packageName;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void writeExternal(ObjectOutput o) throws IOException {
    IOUtils.writeUTF(o, name);
    IOUtils.writeUTF(o, packageName);
  }

  public void readExternal(ObjectInput i) throws IOException, ClassNotFoundException {
    this.name = IOUtils.readUTF(i);
    this.packageName = IOUtils.readUTF(i);
  }
}
