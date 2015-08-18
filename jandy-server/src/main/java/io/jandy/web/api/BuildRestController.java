package io.jandy.web.api;

import com.google.common.collect.Lists;
import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.java.JavaProfilingDump;
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

  @RequestMapping(value = "/{id}/java", method = RequestMethod.GET)
  public JavaProfilingDump getJavaTreeNodes(@PathVariable long id) throws Exception {

    Build build = buildRepository.findOne(id);
    calculateModelInView(build.getJavaProfilingDump().getRoot().getChildren().get(0), 0);

    return build.getJavaProfilingDump();
  }

  private void calculateModelInView(JavaTreeNode node, int depth) {
    node.setDepth(depth);

    for (JavaTreeNode child : node.getChildren()) {
      calculateModelInView(child, depth+1);
    }
  }
}
