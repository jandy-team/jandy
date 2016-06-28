package io.jandy.domain;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2016-06-14
 */
@Entity
public class ProfException {
  @Id
  @GeneratedValue
  private Long id;

  private String message;

  @ManyToOne
  private ProfClass klass;

  @OneToOne(mappedBy = "exception")
  private ProfTreeNode treeNode;

  public Long getId() {
    return id;
  }

  public ProfException setId(Long id) {
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
