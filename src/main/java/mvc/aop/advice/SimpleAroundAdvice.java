package mvc.aop.advice;

/**
 * simpleBeforeAdvice
 *
 * @author cc
 * @description 这个接口继承了MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice这三个接口，相当于这三个接口的合集。
 * @since 2023/5/23 14:26
 */
public interface SimpleAroundAdvice extends SimpleMethodBeforeAdvice, SimpleAfterReturnAdvice, SimpleThrowAdvice {

}
