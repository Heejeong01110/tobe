package spring.ex.tobe.learningtest.proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;

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

}
