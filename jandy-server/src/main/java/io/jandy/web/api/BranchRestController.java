package io.jandy.web.api;

import com.google.common.collect.Iterables;
import io.jandy.domain.*;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    return buildRepository.findAll(QBuild.build.branch.id.eq(id));
  }
}
