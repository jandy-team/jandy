//package io.jandy.config;
//
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.retry.RetryOneTime;
//import org.apache.curator.test.TestingServer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//
///**
// * @author JCooky
// * @since 2016-07-24
// */
//@Configuration
//public class ZookeeperConfig {
//  @Autowired
//  private Environment env;
//
//  private TestingServer zkServer = null;
//
//  @PostConstruct
//  public void initialize() throws Exception {
//    if (env.getProperty("jandy.zk.type", "embedded").equals("embedded")) {
//      zkServer = new TestingServer();
//      zkServer.start();
//    }
//  }
//
//  @PreDestroy
//  public void terminate() throws IOException {
//    if (zkServer != null) {
//      zkServer.close();
//      zkServer.stop();
//    }
//  }
//
//  @Bean(destroyMethod = "close")
//  public CuratorFramework curatorFramework() {
//    CuratorFramework zk = CuratorFrameworkFactory.newClient(zkServer.getConnectString(), new RetryOneTime(100));
//    zk.start();
//
//    return zk;
//  }
//}
