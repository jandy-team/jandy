package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jandy.util.Color;
import io.jandy.util.ColorUtils;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
@Data
@Accessors(chain = true)
@ToString(exclude = {"commit", "branch", "samples", "profiles"})
public class Build {
  @Id
  @GeneratedValue
  private long id;

  private String startedAt, finishedAt;
  private long duration;
  private String language;
  private long travisBuildId;
  private long number;
  private BuildState state = BuildState.UNKNOWN;

  private int numSamples;
  private int numSucceededSamples;

  @Transient
  private String buildAt;

  @OneToOne(cascade = CascadeType.REMOVE)
  private Commit commit;

  @ManyToOne
  @JsonIgnore
  private Branch branch;

  @ManyToMany
  @JsonIgnore
  private List<Sample> samples = new ArrayList<>();

  @OneToMany(mappedBy = "build", cascade = CascadeType.REMOVE)
  private List<ProfContextDump> profiles = new ArrayList<>();

  public Color getColor() {
    double interp = (double) this.getNumSucceededSamples() / (double) this.getNumSamples();
    return ColorUtils.interpolate(Color.FAILURE, Color.SUCCESS, interp);
  }
}
