package io.jandy.service;

import io.jandy.domain.ProfClass;
import io.jandy.domain.ProfClassRepository;
import io.jandy.domain.ProfMethod;
import io.jandy.domain.ProfMethodRepository;
import io.jandy.domain.data.ClassObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JCooky
 * @since 2016-07-14
 */
@Service
public class ProfService {

  @Autowired
  private ProfClassRepository profClassRepository;
  @Autowired
  private ProfMethodRepository profMethodRepository;

  @Transactional(readOnly = true)
  public ProfClass findClass(String name, String packageName) {
    ProfClass klass = profClassRepository.findByNameAndPackageName(name, packageName);
    if (klass == null) {
      klass = createClass(name, packageName);
    }

    return klass;
  }

  @Transactional
  public synchronized ProfClass createClass(String name, String packageName) {
    ProfClass klass = new ProfClass();
    klass.setName(name);
    klass.setPackageName(packageName);
    return profClassRepository.save(klass);
  }

  @Transactional(readOnly = true)
  public ProfMethod findMethod(String name, String descriptor, int access, ProfClass klass) {
    ProfMethod method = profMethodRepository.findByNameAndDescriptorAndAccessAndOwner_Id(name, descriptor, access, klass.getId());
    if (method == null) {
      method = createMethod(name, descriptor, access, klass);
    }

    return method;
  }

  @Transactional
  public synchronized ProfMethod createMethod(String name, String descriptor, int access, ProfClass klass) {
    ProfMethod method = new ProfMethod();
    method.setName(name);
    method.setDescriptor(descriptor);
    method.setAccess(access);
    method.setOwner(klass);
    return profMethodRepository.save(method);
  }
}
