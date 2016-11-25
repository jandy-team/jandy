package io.jandy.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "descriptor", "access", "owner_id"}))
public class ProfMethod {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  @Column(length = 512)
  private String descriptor;
  private int access;

  @ManyToOne
  private ProfClass owner;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public ProfClass getOwner() {
    return owner;
  }

  public void setOwner(ProfClass owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("name", name)
        .append("descriptor", descriptor)
        .append("owner", owner)
        .toString();
  }

  public void setAccess(int access) {
    this.access = access;
  }

  public int getAccess() {
    return access;
  }
}
