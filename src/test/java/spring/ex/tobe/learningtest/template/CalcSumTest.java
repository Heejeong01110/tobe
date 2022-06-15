package spring.ex.tobe.learningtest.template;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CalcSumTest {

  private static String path;
  private static Calculator calculator;

  @BeforeAll
  static void setUp() {
    path = CalcSumTest.class.getClassLoader().getResource("numbers.txt").getPath();
    calculator = new Calculator();
  }

  @Test
  void sumOfNumbers() throws IOException {
    assertThat(calculator.calcSum(path), is(10));
  }

  @Test
  void multiplyOfNumbers() throws IOException {
    assertThat(calculator.calcMultiply(path), is(24));
  }

  @Test
  void concentrateString() throws IOException {
    assertThat(calculator.concentrate(path), is("1234"));
  }
}
