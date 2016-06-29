package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by syjsmk on 6/29/16.
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, String>{
}
