package io.jandy.web;

import io.jandy.domain.*;
import io.jandy.util.api.GitHubApi;
import io.jandy.util.api.json.GHUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author JCooky
 * @since 2015-07-10
 */
@Controller
@RequestMapping("/prof")
public class BenchmarkController {

  @Autowired
  private GitHubApi github;

  @RequestMapping("/{id:\\d+}")
  public ModelAndView getProf(@PathVariable("id") ProfContextDump prof) throws IOException {
    Build build = prof.getBuild();

    GHUser user = null;
    if (build.getCommit() != null)
      user = github.getUser(build.getCommit().getCommitterName());

    return new ModelAndView("benchmark")
        .addObject("build", build)
        .addObject("prof", prof)
        .addObject("committerAvatarUrl", user == null ? null : user.getAvatarUrl());
  }
}
