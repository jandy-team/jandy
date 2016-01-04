package io.jandy.web.api;

import io.jandy.domain.*;
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
@RequestMapping("/rest/prof")
public class BuildRestController {

  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ProfContextDump getProfContextDump(@PathVariable long id) throws Exception {

    return profContextDumpRepository.findOne(id);
  }
}
