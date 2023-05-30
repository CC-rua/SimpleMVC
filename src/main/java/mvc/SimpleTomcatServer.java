package mvc;

import lombok.extern.slf4j.Slf4j;
import mvc.sevlet.DispatcherServlet;
import mvc.util.customException.FileException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.jasper.servlet.JspServlet;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * SimpleTomcatServer
 *
 * @author cc
 * @description
 * @since 2023/5/29 15:31
 */
@Slf4j
public class SimpleTomcatServer implements Server {

    private Tomcat tomcat;

    public SimpleTomcatServer(Configuration configuration) {
        try {
            tomcat = new Tomcat();
            tomcat.setBaseDir(configuration.getDocBase());
            tomcat.setPort(configuration.getServerPort());
            File rootFolder = getRootFolder();
            File webContentFolder = new File(rootFolder.getAbsolutePath(), configuration.getResourcePath());
            if (!webContentFolder.exists()) {
                webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
            }
            log.info("Tomcat:configuring app with basedir: [{}]", webContentFolder.getAbsolutePath());
            StandardContext ctx = (StandardContext) tomcat.addWebapp(configuration.getContextPath(), webContentFolder.getAbsolutePath());
            ctx.setParentClassLoader(this.getClass().getClassLoader());
            WebResourceRoot resources = new StandardRoot(ctx);
            ctx.setResources(resources);
            // 添加 jspServlet，defaultServlet 和自己实现的 dispatcherServlet
            tomcat.addServlet("", "jspServlet", new JspServlet()).setLoadOnStartup(3);
            tomcat.addServlet("", "defaultServlet", new DefaultServlet()).setLoadOnStartup(1);
            tomcat.addServlet("", "dispatcherServlet", new DispatcherServlet()).setLoadOnStartup(0);
            ctx.addServletMappingDecoded("/templates/" + "*", "jspServlet");
            ctx.addServletMappingDecoded("/static/" + "*", "defaultServlet");
            ctx.addServletMappingDecoded("/*", "dispatcherServlet");
        } catch (Exception e) {
            log.error("初始化 Tomcat 失败", e);
            e.printStackTrace();
        }

    }

    @Override
    public void startServer() throws LifecycleException {
        tomcat.start();
        String address = tomcat.getServer().getAddress();
        int port = tomcat.getConnector().getPort();
        log.info("local address: http://{}:{}", address, port);
        tomcat.getServer().await();
    }

    @Override
    public void stopServer() throws LifecycleException {
        tomcat.stop();
    }

    private File getRootFolder() {
        try {
            File root;
            String runningJarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            log.info("Tomcat:application resolved root folder: [{}]", root.getAbsolutePath());
            return root;
        } catch (URISyntaxException ex) {
            throw new FileException("URI exception");
        }
    }
}
