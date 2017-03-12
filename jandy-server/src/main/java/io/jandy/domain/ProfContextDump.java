package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
@Data
@Accessors(chain = true)
public class ProfContextDump {
  @Id
  @GeneratedValue
  private long id;

  private ProfContextState state;
  private long maxTotalDuration;
  // elapsed duration to be compare prev build
  private Long elapsedDuration;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "profContext")
  @JsonIgnore
  private List<ProfThread> threads = new ArrayList<>();

  @ManyToOne
  @JsonIgnore
  private Sample sample;

  @ManyToOne
  @JsonIgnore
  private Build build;

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("maxTotalDuration", maxTotalDuration)
        .append("elapsedDuration", elapsedDuration)
        .append("build_id", build.getId())
        .append("sample_id", sample.getId())
        .toString();
  }
}
