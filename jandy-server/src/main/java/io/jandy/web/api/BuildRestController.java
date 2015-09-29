package io.jandy.web.api;

import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.ProfContextDump;
import io.jandy.domain.ProfTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JCooky
 * @since 2015-07-23
 */
@RestController
@RequestMapping("/rest/build")
public class BuildRestController {

  @Autowired
  private BuildRepository buildRepository;

  @RequestMapping(value = "/{id}/prof", method = RequestMethod.GET)
  public ProfContextDump getJavaTreeNodes(@PathVariable long id) throws Exception {

    ProfContextDump profContextDump = buildRepository.findOne(id).getProfContextDump();
    calculateModelInView(profContextDump.getRoot().getChildren().get(0), 0);

    return profContextDump;
  }

  private void calculateModelInView(ProfTreeNode node, int depth) {
    node.setDepth(depth);

    for (ProfTreeNode child : node.getChildren()) {
      calculateModelInView(child, depth+1);
    }
  }
}
