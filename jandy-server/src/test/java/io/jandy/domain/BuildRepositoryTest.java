package io.jandy.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author JCooky
 * @since 2016-11-19
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("jpatest")
public class BuildRepositoryTest {

  @Autowired
  private BuildRepository buildRepository;

  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;

  @Test
  public void testDeleteProfilesById() throws Exception {
    Build build = new Build();
    build.setTravisBuildId(1L);
    build = buildRepository.save(build);

    ProfContextDump prof1 = new ProfContextDump(),
        prof2 = new ProfContextDump();

    prof1.setBuild(build);
    prof1 = profContextDumpRepository.save(prof1);
    prof2.setBuild(build);
    prof2 = profContextDumpRepository.save(prof2);

    long numDeletes = buildRepository.deleteProfilesById(build.getId());

    assertEquals(numDeletes, 2);
  }

}