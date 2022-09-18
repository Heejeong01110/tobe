package spring.ex.tobe.learningtest.proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

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
