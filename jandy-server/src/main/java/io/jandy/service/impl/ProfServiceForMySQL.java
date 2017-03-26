package io.jandy.service.impl;

import io.jandy.domain.data.ClassObject;
import io.jandy.domain.data.ExceptionObject;
import io.jandy.domain.data.MethodObject;
import io.jandy.domain.data.TreeNode;
import io.jandy.service.ProfService;
import io.jandy.util.sql.BatchUpdateQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.jandy.util.sql.Query.*;
import static io.jandy.util.sql.Query.select;
import static io.jandy.util.sql.Query.subQuery;
import static io.jandy.util.sql.conditional.Where.eq;

/**
 * Created by naver on 2017. 3. 20..
 */
@Service
@Profile("mysql")
public class ProfServiceForMySQL extends ProfService {
    @Autowired
    private JdbcTemplate jdbc;

    @Transactional
    protected void doUpdateTreeNodes0(List<TreeNode> treeNodes) {
        Set<MethodObject> methodObjects = treeNodes.stream().map(TreeNode::getMethod).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<ClassObject> classObjects = treeNodes.stream().map(TreeNode::getMethod).filter(Objects::nonNull).map(MethodObject::getOwner).filter(Objects::nonNull).collect(Collectors.toSet());
        List<ExceptionObject> exceptionObjects = treeNodes.stream().map(n -> n.getAcc() == null ? null : n.getAcc().getException()).filter(Objects::nonNull).collect(Collectors.toList());

        if (classObjects.size() > 0) {
            insert().ignore().into("prof_class")
                    .columns("name", "package_name")
                    .values(classObjects.stream().map(co -> new Object[] {co.getName(), co.getPackageName()}))
                    .execute(jdbc);
        }

        if (methodObjects.size() > 0) {
            insert().ignore().into("prof_method")
                    .columns("name", "access", "descriptor", "owner_id")
                    .values(methodObjects.stream().map(mo -> new Object[]{
                            mo.getName(), mo.getAccess(), mo.getDescriptor(),
                            subQuery(
                                    select().columns("id").from("prof_class").where(
                                            eq("name", mo.getOwner().getName()).and(eq("package_name", mo.getOwner().getPackageName()))
                                    )
                            )
                    }))
                    .execute(jdbc);
        }

        if (exceptionObjects.size() > 0) {
            insert().ignore().into("prof_exception")
                    .columns("id", "message", "klass_id")
                    .values(exceptionObjects.stream().map(eo -> new Object[]{
                            eo.getId(), eo.getMessage(),
                            subQuery(
                                    select().columns("id").from("prof_class").where(
                                            eq("name", eo.getKlass().getName()).and(eq("package_name", eo.getKlass().getPackageName()))
                                    )
                            )
                    }))
                    .execute(jdbc);
        }

        Set<String> ids = treeNodes.stream().map(TreeNode::getParentId).filter(Objects::nonNull).collect(Collectors.toSet());
        ids.addAll(treeNodes.stream().map(TreeNode::getId).filter(Objects::nonNull).collect(Collectors.toSet()));

        if (ids.size() > 0) {
            insert().ignore().into("prof_tree_node").columns("id", "elapsed_time", "root", "start_time")
                    .values(ids.stream().map(id -> new Object[]{
                            id, 0, 0, 0
                    }))
                    .execute(jdbc);
        }

        if (treeNodes.size() > 0) {
            BatchUpdateQueryBuilder<String> q = update("prof_tree_node");
            for (TreeNode treeNodeData : treeNodes) {
                String id = treeNodeData.getId();
                MethodObject mo = treeNodeData.getMethod();
                ExceptionObject eo = treeNodeData.getAcc() == null ? null : treeNodeData.getAcc().getException();

                q
                        .set(id, "elapsed_time", treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getElapsedTime())
                        .set(id, "start_time", treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getStartTime())
                        .set(id, "root", treeNodeData.isRoot())
                        .set(id, "exception_id", eo == null ? null : eo.getId())
                        .set(id, "method_id", mo == null ? null : subQuery(
                                select().columns("id").from("prof_method").where(
                                        eq("access", mo.getAccess()).and(
                                                eq("descriptor", mo.getDescriptor()).and(
                                                        eq("name", mo.getName()).and(
                                                                eq("owner_id", mo.getOwner() == null ? null : subQuery(
                                                                        select().columns("id").from("prof_class").where(
                                                                                eq("name", mo.getOwner().getName()).and(eq("package_name", mo.getOwner().getPackageName()))
                                                                        )
                                                                ))
                                                        )
                                                )
                                        )
                                )
                        ))
                        .set(id, "parent_id", treeNodeData.getParentId());
            }

            q.execute(jdbc);
        }
    }
}
