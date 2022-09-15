package spring.ex.tobe.learningtest.proxy;

import java.text.MessageFormat;

public class HelloTarget implements Hello{

  @Override
  public String sayHello(String name) {
    return MessageFormat.format("Hello {0}", name);
  }

  @Override
  public String sayHi(String name) {
    return MessageFormat.format("Hi {0}", name);
  }

  @Override
  public String sayThankYou(String name) {
    return MessageFormat.format("ThankYou {0}", name);
  }
}
