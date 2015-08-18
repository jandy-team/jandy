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
public class MethodKey implements Externalizable {
  private ClassKey owner;
  private int access;
  private String name;
  private String descriptor;

  public ClassKey getOwner() {
    return owner;
  }

  public void setOwner(ClassKey owner) {
    this.owner = owner;
  }

  public int getAccess() {
    return access;
  }

  public void setAccess(int access) {
    this.access = access;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescriptor() {
    return descriptor;
  }

  public void setDescriptor(String descriptor) {
    this.descriptor = descriptor;
  }

  public void writeExternal(ObjectOutput o) throws IOException {
    o.writeObject(owner);
    o.writeInt(access);
    IOUtils.writeUTF(o, name);
    IOUtils.writeUTF(o, descriptor);
  }

  public void readExternal(ObjectInput i) throws IOException, ClassNotFoundException {
    owner = (ClassKey)i.readObject();
    access = i.readInt();
    name = IOUtils.readUTF(i);
    descriptor = IOUtils.readUTF(i);
  }
}
