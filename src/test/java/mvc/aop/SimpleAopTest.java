package mvc.aop;

import mvc.aop.advice.SimpleAdviceBase;
import mvc.container.SimpleBeanContainer;
import mvc.ioc.SimpleIoc;
import mvc.util.InstanceFactory;
import mvc.util.test.TestClassA;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.verify;

public class SimpleAopTest {

    private SimpleAop simpleAopUnderTest;
    private SimpleBeanContainer beanContainer;

    @Before
    public void setUp() {
        beanContainer = InstanceFactory.getInstance(SimpleBeanContainer.class);
        SimpleIoc simpleIocUnderTest = new SimpleIoc();
        beanContainer.loadBean("mvc.util.test");
        simpleIocUnderTest.doIco();
        simpleAopUnderTest = new SimpleAop();
    }

    @Test
    public void testAop() {
        // Setup
        final SimpleAdviceBase advice = null;

        // Run the test
        simpleAopUnderTest.doAop();
        TestClassA bean = beanContainer.getBean(TestClassA.class);
        bean.sayHello();
        bean.shark();
        // Verify the results
        verify(bean).sayHello();
    }
}
