package mvc.util;


import mvc.util.test.TestClassA;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClassUtilTest {


    @Test
    public void testGetClassesByPackage() {
        List<? extends Class<?>> classesByPackage = ClassUtil.getClassesByPackage("mvc.util.test");
        Assert.assertEquals(List.of(TestClassA.class), classesByPackage);
    }


}
