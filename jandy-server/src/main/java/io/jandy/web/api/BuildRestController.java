package io.jandy.web.api;

import com.google.common.collect.Lists;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.java.JavaTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author JCooky
 * @since 2015-07-23
 */
@RestController
@RequestMapping("/rest/builds")
public class BuildRestController {

  @Autowired
  private BuildRepository buildRepository;

  @RequestMapping(value = "/{id}/java/nodes", method = RequestMethod.GET)
  public Collection<JavaTreeNode> getJavaTreeNodes(@PathVariable long id) throws Exception {
    return Lists.newArrayList(buildRepository.findOne(id).getJavaProfilingDump());
  }
}
