package spring.ex.tobe.learningtest.proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Proxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

class HelloTargetTest {

  @Test
  public void simpleProxy() {
    Hello hello = new HelloTarget();
    assertThat(hello.sayHello("Toby"), is("Hello Toby"));
    assertThat(hello.sayHi("Toby"), is("Hi Toby"));
    assertThat(hello.sayThankYou("Toby"), is("ThankYou Toby"));

    Hello proxyHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),
        new Class[]{Hello.class},
        new HelloUppercase(new HelloTarget()));
    assertThat(proxyHello.sayHello("Toby"), is("HELLO TOBY"));
    assertThat(proxyHello.sayHi("Toby"), is("HI TOBY"));
    assertThat(proxyHello.sayThankYou("Toby"), is("THANKYOU TOBY"));
  }

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

  private static class UppercaseAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      String ret = (String) invocation.proceed();
      return ret.toUpperCase();
    }
  }
}
