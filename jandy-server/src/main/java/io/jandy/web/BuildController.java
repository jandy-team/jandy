package io.jandy.web;

import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.java.JavaTreeNode;
import io.jandy.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author JCooky
 * @since 2015-07-10
 */
@Controller
@RequestMapping("/build")
public class BuildController {
  @Autowired
  private BuildRepository buildRepository;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ModelAndView build(@PathVariable long id) {
    Build build = buildRepository.findOne(id);

    if (build == null)
      throw new ResourceNotFoundException();
    calculateModelInView(build.getJavaProfilingDump().getRoot().getChildren().get(0), 0, 0.0, 1.0);

    return new ModelAndView("build")
        .addObject("build", build);
  }

  private void calculateModelInView(JavaTreeNode node, int depth, double offset, double parentWidth) {
    node.setDepth(depth);
    if (node.getParent() != null) {
      node.setOffset(offset);
      if (node.getParent().getTotalDuration() > 0)
        node.setWidth(((double)node.getTotalDuration() / (double)node.getParent().getTotalDuration()) * parentWidth);
      else
        node.setWidth(parentWidth);
    }
    offset = node.getOffset();
    for (JavaTreeNode child : node.getChildren()) {
      calculateModelInView(child, depth+1, offset, node.getWidth());
      offset += child.getWidth();
    }
  }
}
