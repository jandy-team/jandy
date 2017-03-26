package io.jandy.service.impl;

import io.jandy.domain.*;
import io.jandy.domain.data.*;
import io.jandy.service.ProfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by naver on 2017. 3. 20..
 */
@Service
@Profile("!mysql")
public class ProfServiceForJPA extends ProfService {

    @Autowired
    private ProfClassRepository profClassRepository;
    @Autowired
    private ProfMethodRepository profMethodRepository;
    @Autowired
    private ProfTreeNodeRepository profTreeNodeRepository;
    @Autowired
    private ProfExceptionRepository profExceptionRepository;

    @Override
    @Transactional
    protected void doUpdateTreeNodes0(List<TreeNode> treeNodes) {
        Set<MethodObject> methodObjects = treeNodes.stream().map(TreeNode::getMethod).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<ClassObject> classObjects = treeNodes.stream().map(TreeNode::getMethod).filter(Objects::nonNull).map(MethodObject::getOwner).filter(Objects::nonNull).collect(Collectors.toSet());
        List<ExceptionObject> exceptionObjects = treeNodes.stream().map(n -> n.getAcc() == null ? null : n.getAcc().getException()).filter(Objects::nonNull).collect(Collectors.toList());

        if (classObjects.size() > 0) {
            for (ClassObject co : classObjects) {
                if (null == profClassRepository.findByNameAndPackageName(co.getName(), co.getPackageName())) {
                    ProfClass profClass = new ProfClass();
                    profClass.setName(co.getName());
                    profClass.setPackageName(co.getPackageName());
                    profClassRepository.save(profClass);
                }
            }
        }

        if (methodObjects.size() > 0) {
            for (MethodObject mo : methodObjects) {
                ProfClass profClass = profClassRepository.findByNameAndPackageName(mo.getOwner().getName(), mo.getOwner().getPackageName());
                if (null == profMethodRepository.findByNameAndDescriptorAndAccessAndOwner_Id(mo.getName(), mo.getDescriptor(), mo.getAccess(), profClass.getId())) {
                    ProfMethod profMethod = new ProfMethod();
                    profMethod.setOwner(profClass);
                    profMethod.setName(mo.getName());
                    profMethod.setAccess(mo.getAccess());
                    profMethod.setDescriptor(mo.getDescriptor());
                    profMethodRepository.save(profMethod);
                }
            }
        }

        Map<String, ProfException> profExceptions = new HashMap<>();

        if (exceptionObjects.size() > 0) {
            for (ExceptionObject eo : exceptionObjects) {
                ProfClass profClass = profClassRepository.findByNameAndPackageName(eo.getKlass().getName(), eo.getKlass().getPackageName());
                if (profClass == null) {
                    profClass = new ProfClass();
                    profClass.setName(eo.getKlass().getName());
                    profClass.setPackageName(eo.getKlass().getPackageName());
                    profClass = profClassRepository.save(profClass);
                }
                ProfException profException = new ProfException();
                profException.setId(eo.getId());
                profException.setKlass(profClass);
                profException.setMessage(eo.getMessage());
                profExceptions.put(eo.getId(), profExceptionRepository.save(profException));
            }
        }

        Set<String> ids = treeNodes.stream().map(TreeNode::getParentId).filter(Objects::nonNull).collect(Collectors.toSet());
        ids.addAll(treeNodes.stream().map(TreeNode::getId).filter(Objects::nonNull).collect(Collectors.toSet()));

        Map<String, ProfTreeNode> parents = new HashMap<>();
        if (ids.size() > 0) {
            for (String id : ids) {
                ProfTreeNode profTreeNode = new ProfTreeNode();
                profTreeNode.setId(id);
                parents.put(id, profTreeNodeRepository.save(profTreeNode));
            }
        }

        if (treeNodes.size() > 0) {
            for (TreeNode treeNodeData : treeNodes) {
                MethodObject method = treeNodeData.getMethod();
                ClassObject owner = method == null ? null : method.getOwner();
                Accumulator acc = treeNodeData.getAcc();

                ProfClass profClass = owner == null ? null : profClassRepository.findByNameAndPackageName(
                        owner.getName(),
                        owner.getPackageName());
                ProfMethod profMethod = method == null ? null : profMethodRepository.findByNameAndDescriptorAndAccessAndOwner_Id(
                        method.getName(),
                        method.getDescriptor(),
                        method.getAccess(),
                        profClass == null ? null : profClass.getId());
                ExceptionObject eo = acc == null ? null : acc.getException();

                ProfTreeNode profTreeNode = parents.get(treeNodeData.getId());
                profTreeNode.setMethod(profMethod);
                profTreeNode.setException(eo == null ? null : profExceptions.get(eo.getId()));
                profTreeNode.setElapsedTime(acc == null ? 0L : acc.getElapsedTime());
                profTreeNode.setStartTime(acc == null ? 0L : acc.getStartTime());
                profTreeNode.setParent(parents.get(treeNodeData.getParentId()));
                profTreeNode.setRoot(treeNodeData.isRoot());
                profTreeNodeRepository.save(profTreeNode);
            }
        }
    }
}
