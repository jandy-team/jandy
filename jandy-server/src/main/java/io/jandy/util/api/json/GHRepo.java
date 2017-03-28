package io.jandy.util.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-04-08
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHRepo implements Serializable {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("owner")
  private GHUser owner;

  @JsonProperty("name")
  private String name;

  @JsonProperty("full_name")
  private String fullName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("default_branch")
  private String defaultBranch;

  public Long getId() {
    return id;
  }

  public GHRepo setId(Long id) {
    this.id = id;
    return this;
  }

  public GHUser getOwner() {
    return owner;
  }

  public GHRepo setOwner(GHUser owner) {
    this.owner = owner;
    return this;
  }

  public String getName() {
    return name;
  }

  public GHRepo setName(String name) {
    this.name = name;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public GHRepo setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public GHRepo setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getDefaultBranch() {
    return defaultBranch;
  }

  public GHRepo setDefaultBranch(String defaultBranch) {
    this.defaultBranch = defaultBranch;
    return this;
  }
}
