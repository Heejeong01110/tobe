package spring.ex.tobe.learningtest.template;

public interface LineCallback<T> {

  T doSomethingWithLine(String Line, T value);
}
