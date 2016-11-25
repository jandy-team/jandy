package io.jandy.domain;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2016-06-14
 */
@Entity
public class ProfException {
  @Id
  private String id;

  @Column(length = 1024)
  private String message;

  @ManyToOne
  private ProfClass klass;

  @OneToOne(mappedBy = "exception")
  private ProfTreeNode treeNode;

  public String getId() {
    return id;
  }

  public ProfException setId(String id) {
    this.id = id;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public ProfException setMessage(String message) {
    this.message = message;
    return this;
  }

  public ProfClass getKlass() {
    return klass;
  }

  public ProfException setKlass(ProfClass klass) {
    this.klass = klass;
    return this;
  }
}
