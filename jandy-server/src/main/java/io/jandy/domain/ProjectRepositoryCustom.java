package io.jandy.domain;

/**
 * @author jcooky
 */
public interface ProjectRepositoryCustom {
  Project findByBuild(Build build);
}
