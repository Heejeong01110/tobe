package spring.ex.tobe.learningtest.proxy;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {
public void setMappedClassName(String mappedClassName){
  this.setClassFilter(new SimpleClassFilter(mappedClassName));
}

  static class SimpleClassFilter implements ClassFilter {
    String mappedClassName;
    public SimpleClassFilter(String mappedClassName) {
      this.mappedClassName = mappedClassName;
    }

    @Override
    public boolean matches(Class<?> clazz) {
      return PatternMatchUtils.simpleMatch(mappedClassName, clazz.getSimpleName());
    }
  }
}
