package mvc;

import lombok.Getter;
import mvc.aop.SimpleAop;
import mvc.container.SimpleBeanContainer;
import mvc.ioc.SimpleIoc;
import mvc.sevlet.ControllerDespatcher;
import mvc.util.InstanceFactory;

/**
 * SimpleMvc
 *
 * @author cc
 * @description
 * @since 2023/5/29 16:20
 */
public class SimpleMvc {
    /**
     * 配置文件
     */
    @Getter
    private static Configuration configuration;

    /**
     * 服务器对象
     */
    private static Server server;

    /**
     * 通过配置文件启动
     *
     * @param configuration 配置项目
     */
    public static void run(Configuration configuration) {
        SimpleMvc.start(configuration);
    }

    /**
     * 启动
     */
    public static void run(Class<?> bootClass) {
        run(Configuration.builder().bootClass(bootClass).build());
    }

    /**
     * 启动
     */
    public static void run(Class<?> bootClass, int port) {
        run(Configuration.builder().bootClass(bootClass).serverPort(port).build());
    }


    private static void start(Configuration configuration) {
        try {

            SimpleMvc.configuration = configuration;
            String basePackage = configuration.getBootClass().getPackage().getName();
            SimpleBeanContainer beanContainer = InstanceFactory.getInstance(SimpleBeanContainer.class);
            beanContainer.loadBean(basePackage);
            //注意 Aop 必须在 Ioc 之前执行
            SimpleAop aop = InstanceFactory.getInstance(SimpleAop.class);
            aop.doAop();
            SimpleIoc ioc = InstanceFactory.getInstance(SimpleIoc.class);
            ioc.doIoc();
            ControllerDespatcher controllerDespatcher = InstanceFactory.getInstance(ControllerDespatcher.class);
            controllerDespatcher.doInit();

            SimpleMvc.server = new SimpleTomcatServer(configuration);
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
