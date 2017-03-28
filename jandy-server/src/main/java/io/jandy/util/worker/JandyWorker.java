package io.jandy.util.worker;

import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.SerializedConverter;
import io.jandy.domain.data.ProfilingContext;
import io.jandy.domain.data.TreeNode;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2016-07-24
 */
@Service
public class JandyWorker {
  private static final Logger log = LoggerFactory.getLogger(JandyWorker.class);

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private Environment env;

  private Map<Long, ObjectQueue<JandyTask>> queues = Collections.synchronizedMap(new HashMap<>());

  @Async
  public void start(long travisBuildId, Class<? extends JandyTaskExecutor> taskExecutorClass) {
    JandyTaskExecutor executor = applicationContext.getBean(taskExecutorClass);

    File parent = new File("/tmp/"+env.getProperty("spring.application.name", "jandy")),
      stored = new File(parent, "build-"+travisBuildId);

    parent.mkdirs();

    FileObjectQueue<JandyTask> q = null;
    try {
      q = new FileObjectQueue<>(stored, new SerializedConverter<>());

      queues.put(travisBuildId, q);

      ThreadLocal<Boolean> stop = new ThreadLocal<>();
      stop.set(false);
      while (!stop.get()) {
        JandyTask jandyTask = q.peek();

        if (jandyTask != null) {

          executor.execute(jandyTask, () -> stop.set(true));
//          switch (jandyTask.getType()) {
//            case JandyTask.UPDATE:
//              profService.doUpdateTreeNodes((List<TreeNode>) jandyTask.getData());
//              break;
//            case JandyTask.SAVE:
//              profService.doSaveProf((ProfilingContext) jandyTask.getData());
//              break;
//            case JandyTask.FINISH:
//              profService.doFinish((Long) jandyTask.getData());
//              stop = true;
//              break;
//            default:
//              throw new IllegalStateException();
//          }

          q.remove();
        } else {
          Thread.sleep(1);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (q != null)
        q.close();
      queues.remove(travisBuildId);
      FileUtils.deleteQuietly(stored);
    }
  }

  public void put(long buildId, int type, Object data) throws Exception {
    queues.get(buildId).add(new JandyTask(type, (Serializable)data));
  }


}
