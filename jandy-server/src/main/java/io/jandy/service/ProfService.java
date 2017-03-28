package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.domain.data.*;
import io.jandy.util.api.TravisApi;

import io.jandy.util.api.json.TCommit;
import io.jandy.util.api.json.TResult;
import io.jandy.util.worker.JandyTask;
import io.jandy.util.worker.JandyTaskExecutionContext;
import io.jandy.util.worker.JandyTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-07-14
 */
public abstract class ProfService implements JandyTaskExecutor {
    private static final Logger log = LoggerFactory.getLogger(ProfService.class);

    @Autowired
    private ProfTreeNodeRepository profTreeNodeRepository;
    @Autowired
    private ProfThreadRepository profThreadRepository;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private ProfExceptionRepository profExceptionRepository;
    @Autowired
    private ProfContextDumpRepository profContextDumpRepository;

    @Autowired
    private TravisApi travisApi;

    @Autowired
    private CommitRepository commitRepository;

    @Transactional
    public void execute(JandyTask task, JandyTaskExecutionContext context) {
        switch (task.getType()) {
            case JandyTask.UPDATE:
                this.doUpdateTreeNodes((List<TreeNode>) task.getData());
                break;
            case JandyTask.SAVE:
                this.doSaveProf((ProfilingContext) task.getData());
                break;
            case JandyTask.FINISH:
                this.doFinish((Long) task.getData());
                context.stop();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void doSaveProf(ProfilingContext profilingContext) {
        long profId = profilingContext.getProfId();

        long processStartTime = Long.MAX_VALUE, processEndTime = 0;
        ProfContextDump prof = profContextDumpRepository.findOne(profId);
        for (ThreadObject to : profilingContext.getThreadObjects()) {
            ProfThread profThread = new ProfThread();
            profThread.setThreadId(to.getThreadId());
            profThread.setThreadName(to.getThreadName());
            profThread.setRoot(profTreeNodeRepository.findOne(to.getRootId()));
            profThread.setProfContext(prof);
            profThread = profThreadRepository.save(profThread);

            ProfTreeNode root = profThread.getRoot();

            root.setElapsedTime(0);
            root.setStartTime(Long.MAX_VALUE);
            for (ProfTreeNode child : root.getChildren()) {
                root.setElapsedTime(root.getElapsedTime() + child.getElapsedTime());
                root.setStartTime(Math.min(child.getStartTime(), root.getStartTime()));
            }

            root = profTreeNodeRepository.save(root);
            processStartTime = Math.min(processStartTime, root.getStartTime());
            processEndTime = Math.max(processEndTime, root.getStartTime() + root.getElapsedTime());

            prof.getThreads().add(profThread);
        }

        prof.setMaxTotalDuration(processEndTime - processStartTime);
        ProfContextDump prev = profContextDumpRepository.findPrev(prof);
        prof.setElapsedDuration(prev == null ? 0L : prof.getMaxTotalDuration() - prev.getMaxTotalDuration());
        prof.setState(ProfContextState.FINISH);

        prof = profContextDumpRepository.save(prof);

        log.debug("threads: {}", prof.getThreads());
        log.info("Finish ProfilingContext ({})", prof.getId());
    }

    private void doUpdateTreeNodes(List<TreeNode> treeNodes) {
        int index = 0;

        while (index < treeNodes.size()) {
            doUpdateTreeNodes0(treeNodes.subList(index, Math.min(treeNodes.size(), index + 500)));
            index += 500;
        }

        log.debug("Finish to update tree nodes");
    }

    protected abstract void doUpdateTreeNodes0(List<TreeNode> treeNodes);

    private void doFinish(long buildId) {
        Build build = buildRepository.findByTravisBuildId(buildId);
        List<ProfContextDump> profiles = profContextDumpRepository.findByBuild(build);

        build.setNumSucceededSamples((int) profiles.stream()
                .filter(s -> s.getElapsedDuration() >= 0)
                .count());

        try {
            boolean checked = false;
            while (!checked) {
                TResult result = travisApi.getBuild(build.getTravisBuildId());
                String state = String.valueOf(result.getBuild().getState());

                if ("failed".equals(state) || "passed".equals(state)) {
                    build.setState(BuildState.valueOf(state.toUpperCase()));
                    build.setCommit(commitRepository.save(toCommitEntity(result.getCommit())));
                    build.setStartedAt(String.valueOf(result.getBuild().getStartedAt()));
                    build.setFinishedAt(String.valueOf(result.getBuild().getFinishedAt()));
                    build.setDuration(result.getBuild().getDuration());

                    checked = true;
                } else if ("created".equals(state) || "started".equals(state)) {
                    Thread.sleep(1);
                } else {
                    throw new IllegalStateException();
                }
            }

            log.trace("Save the build information{buildId: {}}", build);
        } catch (InterruptedException | IOException | IllegalStateException e) {
            log.error(e.getMessage(), e);
        }

        buildRepository.save(build);
    }

    private Commit toCommitEntity(TCommit c) {
        Commit commit = new Commit();
        commit.setCommittedAt(c.getCommittedAt())
                .setCommitterEmail(c.getCommitterEmail())
                .setCommitterName(c.getCommitterName())
                .setMessage(c.getMessage())
                .setSha(c.getSha());

        return commitRepository.save(commit);
    }
}