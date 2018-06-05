package io.jandy.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "descriptor", "access", "owner_id"}))
@Data
@Accessors(chain = true)
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

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("name", name)
        .append("descriptor", descriptor)
        .append("owner", owner)
        .toString();
  }
}
