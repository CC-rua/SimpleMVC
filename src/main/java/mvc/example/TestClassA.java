package mvc.example;

import lombok.extern.slf4j.Slf4j;
import mvc.container.annotation.SimpleComponent;
import mvc.ioc.annotation.SimpleAutowired;

/**
 * TestClass
 *
 * @author cc
 * @description
 * @since 2023/5/22 17:23
 */
@SimpleComponent
@Slf4j
public class TestClassA implements TestInterfaceA {
    @SimpleAutowired
    public TestClassB b;

    public void sayHello() {
        log.info("{} sayHello", this.getClass().getName());
    }

    @Override
    public void shark() {
        log.info("{} shark", this.getClass().getName());
    }
}
