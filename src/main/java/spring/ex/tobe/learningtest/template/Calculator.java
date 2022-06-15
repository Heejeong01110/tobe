package spring.ex.tobe.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

  public int fileReadTemplate(String path, BufferedReaderCallback callback) throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path));
      int res = callback.doSomethingWithReader(br);
      return res;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw e;
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  public int lineReadTemplate(String path, LineCallback callback, int initVal)
      throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path));
      int res = initVal;
      String line = null;
      while ((line = br.readLine()) != null) {
        res = callback.doSomethingWithLine(line, res);
      }
      return res;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw e;
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  public int calcSum(String path) throws IOException {
    LineCallback callback = (line, value) -> value + Integer.parseInt(line);
    return lineReadTemplate(path, callback, 0);
  }

  public int calcMultiply(String path) throws IOException {
    LineCallback callback = (line, value) -> value * Integer.parseInt(line);
    return lineReadTemplate(path, callback, 1);
  }
}
