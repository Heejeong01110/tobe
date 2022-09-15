package spring.ex.tobe.learningtest.jdk;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

  @Test
  public void invokeMethod()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String name = "Spring";

    Method method = name.getClass().getMethod("length");
    assertThat((Integer) method.invoke(name),is(6));

    method = name.getClass().getMethod("charAt", int.class);
    assertThat((Character) method.invoke(name,0),is('S'));
  }
}
