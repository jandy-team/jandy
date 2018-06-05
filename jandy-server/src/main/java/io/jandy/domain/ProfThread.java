package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2016-06-30
 */
@Entity
@Data
@Accessors(chain = true)
public class ProfThread {
  @Id
  @GeneratedValue
  private long id;

  private long threadId;
  private String threadName;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JsonIgnore
  private ProfTreeNode root;

  @ManyToOne
  @JsonIgnore
  private ProfContextDump profContext;
}
