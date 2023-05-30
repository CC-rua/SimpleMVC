package mvc;

import org.apache.catalina.LifecycleException;

/**
 * Server
 *
 * @author cc
 * @description
 * @since 2023/5/29 15:31
 */
public interface Server {
    /**
     * 开启服务
     */
    void startServer() throws LifecycleException;

    /**
     * 结束服务
     */
    void stopServer() throws LifecycleException;
}
