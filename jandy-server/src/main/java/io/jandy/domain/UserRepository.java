package io.jandy.domain;

import io.jandy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByGitHubId(long id);
}
