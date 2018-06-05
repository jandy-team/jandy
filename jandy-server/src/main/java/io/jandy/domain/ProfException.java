package io.jandy.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2016-06-14
 */
@Entity
@Data
@Accessors(chain = true)
public class ProfException {
  @Id
  private String id;

  @Column(length = 1024)
  private String message;

  @ManyToOne
  private ProfClass klass;

  @OneToOne(mappedBy = "exception")
  private ProfTreeNode treeNode;

}
