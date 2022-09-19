package spring.ex.tobe.learningtest.proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import spring.ex.tobe.learningtest.pointcut.Bean;
import spring.ex.tobe.learningtest.pointcut.Target;

class HelloTargetTest {

  @Test
  public void proxyFactoryBean() {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(new HelloTarget());
    pfBean.addAdvice(new UppercaseAdvice());

    Hello proxyHello = (Hello) pfBean.getObject();
    assertThat(proxyHello.sayHello("Toby"), is("HELLO TOBY"));
    assertThat(proxyHello.sayHi("Toby"), is("HI TOBY"));
    assertThat(proxyHello.sayThankYou("Toby"), is("THANKYOU TOBY"));
  }

  @Test
  public void pointcutAdvisor() {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(new HelloTarget());

    NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
    pointcut.setMappedName("sayH*");
    pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
    //advisor : advice와 포인트컷을 둘다 가지고 있는 개념

    Hello proxyHello = (Hello) pfBean.getObject();
    assertThat(proxyHello.sayHello("Toby"), is("HELLO TOBY"));
    assertThat(proxyHello.sayHi("Toby"), is("HI TOBY"));
    assertThat(proxyHello.sayThankYou("Toby"), is("ThankYou Toby"));
  }

  @Test
  public void classNamePointcutAdvisor() {
    NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
      @Override
      public ClassFilter getClassFilter() {
        return clazz -> clazz.getSimpleName().startsWith("HelloT");
      }
    };
    classMethodPointcut.setMappedName("sayH*");

    checkAdviced(new HelloTarget(), classMethodPointcut, true);
  }

  @Test
  public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(public int "
        + "spring.ex.tobe.learningtest.pointcut.Target.minus(int, int) "
        + "throws java.lang.RuntimeException)");

    assertThat(
        pointcut.getClassFilter().matches(Target.class) && pointcut.getMethodMatcher().matches(
            Target.class.getMethod("minus", int.class, int.class), null), is(true));

    assertThat(
        pointcut.getClassFilter().matches(Target.class) && pointcut.getMethodMatcher().matches(
            Target.class.getMethod("plus", int.class, int.class), null), is(false));

    assertThat(
        pointcut.getClassFilter().matches(Target.class) && pointcut.getMethodMatcher().matches(
            Target.class.getMethod("method"), null), is(false));

  }

  @Test
  public void pointcut() throws Exception {
    targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
  }

  public void pointcutMatches(String expression, Boolean expected, Class<?> clazz,
      String methodName, Class<?>... args) throws Exception {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(expression);

    assertThat(pointcut.getClassFilter().matches(clazz) &&
            pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null),
        is(expected));
  }

  public void targetClassPointcutMatches(String expression, Boolean... expected) throws Exception {
    pointcutMatches(expression, expected[0], Target.class, "hello");
    pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
    pointcutMatches(expression, expected[2], Target.class, "minus", int.class, int.class);
    pointcutMatches(expression, expected[3], Target.class, "plus", int.class, int.class);
    pointcutMatches(expression, expected[4], Target.class, "method");
    pointcutMatches(expression, expected[5], Bean.class, "method");

  }

  private void checkAdviced(HelloTarget target, NameMatchMethodPointcut pointcut,
      boolean adviced) {
    ProxyFactoryBean pfBean = new ProxyFactoryBean();
    pfBean.setTarget(target);
    pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
    Hello proxidHello = (Hello) pfBean.getObject();

    if (adviced) {
      assertThat(proxidHello.sayHello("Toby"), is("HELLO TOBY"));
      assertThat(proxidHello.sayHi("Toby"), is("HI TOBY"));
      assertThat(proxidHello.sayThankYou("Toby"), is("ThankYou Toby"));
    } else {
      assertThat(proxidHello.sayHello("Toby"), is("Hello Toby"));
      assertThat(proxidHello.sayHi("Toby"), is("Hi Toby"));
      assertThat(proxidHello.sayThankYou("Toby"), is("ThankYou Toby"));
    }
  }

  private static class UppercaseAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      String ret = (String) invocation.proceed();
      return ret.toUpperCase();
    }
  }
}
