package mvc.container;

import mvc.util.InstanceFactory;
import mvc.example.TestClassA;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanContainerTest {

    private SimpleBeanContainer simpleBeanContainerUnderTest;

    @Before
    public void setUp() {
        simpleBeanContainerUnderTest = InstanceFactory.getInstance(SimpleBeanContainer.class);
    }

    @Test
    public void testAddBean() {
        // Setup
        // Run the test
        simpleBeanContainerUnderTest.addBean(String.class, "bean");

        // Verify the results
    }

    @Test
    public void testRemoveBean() {
        // Setup
        // Run the test
        simpleBeanContainerUnderTest.removeBean(String.class);

        // Verify the results
    }

    @Test
    public void testGetBean() {
        // Setup
        // Run the test
        final Object result = simpleBeanContainerUnderTest.getBean(String.class);

        // Verify the results
    }

    @Test
    public void testLoadBean() {
        // Setup
        Map<Class<?>, Object> beanContainer = new ConcurrentHashMap<>();
        beanContainer.put(TestClassA.class, new TestClassA());
        // Run the test
        simpleBeanContainerUnderTest.loadBean("mvc.util.test");

        // Verify the results
        Assert.assertEquals(beanContainer.size(), simpleBeanContainerUnderTest.getAllBean().size());
    }
}
