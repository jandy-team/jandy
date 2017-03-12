package io.jandy.domain;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "packageName"}))
@Data
@Accessors(chain = true)
@ToString(exclude = {"methods"})
public class ProfClass {
  @Id
  @GeneratedValue
  private Long id;

  private String packageName = "";
  private String name = "";

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "owner")
  @Expose
  private List<ProfMethod> methods;
}
