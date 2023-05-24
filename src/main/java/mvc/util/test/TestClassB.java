package mvc.util.test;

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
public class TestClassB {
    @SimpleAutowired
    public TestClassA A;
}
