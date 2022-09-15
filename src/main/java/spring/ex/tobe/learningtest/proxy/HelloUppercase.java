package spring.ex.tobe.learningtest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HelloUppercase implements InvocationHandler {

  Object target;

  public HelloUppercase(Hello target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object ret = method.invoke(proxy, args);

    if (ret instanceof String && method.getName().startsWith("say")) {
      return ((String) ret).toUpperCase();
    }
    return ret;
  }
}
