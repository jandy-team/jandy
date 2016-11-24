package io.jandy.web.api;

import com.google.gson.*;
import io.jandy.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;


/**
 * @author JCooky
 * @since 2015-07-23
 */
@RestController
@RequestMapping("/rest/prof")
public class BuildRestController {

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ProfContextDump getProfContextDump(@PathVariable("id") ProfContextDump dump) throws Exception {
    return dump;
  }

  @RequestMapping(value = "/{id}/root", method = RequestMethod.GET)
  public ProfTreeNode getRoot(@PathVariable("id") ProfContextDump dump) throws Exception {
    return getNode(dump.getThreads().get(0).getRoot());
  }

  @RequestMapping(value = "/node/{id}", method = RequestMethod.GET)
  public ProfTreeNode getNode(@PathVariable("id") ProfTreeNode root) throws Exception {
    detach(root, 0);

    return root;
  }

  private void detach(ProfTreeNode node, int depth) {
    if (depth == 20)
      node.setChildren(new ArrayList<>());
    else {
      for (ProfTreeNode child : node.getChildren()) {
        detach(child, depth + 1);
      }
    }
  }
}
