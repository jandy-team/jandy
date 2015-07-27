package io.jandy.web.api;

import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.Project;
import io.jandy.domain.QBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author JCooky
 * @since 2015-07-22
 */
@RestController
@RequestMapping("/rest/branches")
public class BranchRestController {
  @Autowired
  private BuildRepository buildRepository;

  @RequestMapping(value = "/{id}/builds", method = RequestMethod.GET)
  public Iterable<Build> getBuilds(@PathVariable long id) {
    Page<Build> builds = buildRepository.findAll(QBuild.build.branch.id.eq(id), new QPageRequest(0, 10, QBuild.build.number.desc()));

    return builds.getContent();
  }
}
