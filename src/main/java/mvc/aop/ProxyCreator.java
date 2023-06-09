package mvc.aop;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.matcher.ElementMatchers.isOverriddenFrom;

/**
 * 代理类创建器
 */
public final class ProxyCreator {

    private ProxyCreator() {
    }

    /**
     * 当调用createProxy方法时，它接收两个参数：targetClass和proxyAdvisor。targetClass是需要代理的类的Class对象，而proxyAdvisor是代理的行为逻辑的实现。
     * 以下是对createProxy方法的详细解释：
     * 使用ByteBuddy创建一个子类：
     * new ByteBuddy().subclass(targetClass)
     * 这一步使用ByteBuddy库创建一个代理类，该代理类将作为targetClass的子类。
     * 定义要拦截的方法：
     * .method(isOverriddenFrom(targetClass))
     * 使用isOverriddenFrom方法来确定哪些方法需要被拦截。这个方法用于选择那些在targetClass中被重写的方法。
     * 拦截方法并委托给代理逻辑：
     * .intercept(MethodDelegation.to(proxyAdvisor))
     * 使用MethodDelegation类将方法的调用委托给proxyAdvisor对象，它实现了代理的具体逻辑。
     * 创建代理类：
     * .make()
     * 调用make()方法生成代理类的字节码。
     * 加载代理类：
     * .load(targetClass.getClassLoader())
     * 使用targetClass的类加载器来加载代理类。
     * 获取代理类的Class对象：
     * .getLoaded()
     * 获取代理类的Class对象，以便后续使用。
     * 使用默认构造函数实例化代理对象：
     * .getDeclaredConstructor().newInstance()
     * 使用反射获取代理类的默认构造函数，并通过调用newInstance()方法来实例化代理对象。
     * 最终，createProxy方法返回一个代理对象，该对象是通过使用ByteBuddy库动态生成的代理类来创建的。这个代理对象能够拦截并委托被代理类中的方法调用给proxyAdvisor对象，以实现自定义的代理行为。
     * 需要注意的是，createProxy方法声明了多个异常，因为在创建代理对象时可能会抛出NoSuchMethodException、InvocationTargetException、InstantiationException和IllegalAccessException等异常。在调用这个方法时，你需要适当地处理这些异常情况。
     */
    public static Object createProxy(Class<?> targetClass, ProxyAdvisor proxyAdvisor) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        return new ByteBuddy()
                .subclass(targetClass)
                .method(isOverriddenFrom(targetClass))
                .intercept(MethodDelegation.to(proxyAdvisor))
                .make()
                .load(targetClass.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor().newInstance();
    }
}