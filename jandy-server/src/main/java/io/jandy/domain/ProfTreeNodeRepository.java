package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface ProfTreeNodeRepository extends JpaRepository<ProfTreeNode, String> {
  List<ProfTreeNode> findByParent_id(String parentId);
}
