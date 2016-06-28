package io.jandy.service;

import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.SerializedConverter;
import io.jandy.domain.data.ProfilingContext;
import io.jandy.domain.data.TreeNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author JCooky
 * @since 2016-07-24
 */
@Service
public class ProfilingDaemonService {
  private static final Logger logger = LoggerFactory.getLogger(ProfilingDaemonService.class);

  @Autowired
  private ProfService profService;

  @Autowired
  private Environment env;

  private Map<Long, ObjectQueue<Task>> queues = Collections.synchronizedMap(new HashMap<>());

  @Async
  public void start(long travisBuildId) {
    File parent = new File("/tmp/"+env.getProperty("spring.application.name", "jandy")),
      stored = new File(parent, "build-"+travisBuildId);

    parent.mkdirs();

    FileObjectQueue<Task> q = null;
    try {
      q = new FileObjectQueue<>(stored, new SerializedConverter<>());

      queues.put(travisBuildId, q);
      
      boolean stop = false;
      while (!stop) {
        Task task = q.peek();

        if (task != null) {

          switch (task.getType()) {
            case Task.UPDATE:
              profService.doUpdateTreeNodes((List<TreeNode>) task.getData());
              break;
            case Task.SAVE:
              profService.doSaveProf((ProfilingContext) task.getData());
              break;
            case Task.FINISH:
              profService.doFinish((Long) task.getData());
              stop = true;
              break;
            default:
              throw new IllegalStateException();
          }

          q.remove();
        } else {
          Thread.sleep(1);
        }
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } finally {
      if (q != null)
        q.close();
      queues.remove(travisBuildId);
      FileUtils.deleteQuietly(stored);
    }
  }

  public void put(long buildId, int type, Object data) throws Exception {
    queues.get(buildId).add(new Task(type, (Serializable)data));
  }

  public static class Task implements Serializable {
    public static final int UPDATE = 0, SAVE = 1, FINISH = 2;

    private int type;
    private Serializable data;

    public Task(int type, Serializable data) {
      this.type = type;
      this.data = data;
    }

    public Serializable getData() {
      return data;
    }

    public int getType() {
      return type;
    }
  }
}
