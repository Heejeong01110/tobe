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

  public <T> T lineReadTemplate(String path, LineCallback<T> callback, T initVal)
      throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path));
      T res = initVal;
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
    LineCallback<Integer> callback = (line, value) -> value + Integer.parseInt(line);
    return lineReadTemplate(path, callback, 0);
  }

  public int calcMultiply(String path) throws IOException {
    LineCallback<Integer> callback = (line, value) -> value * Integer.parseInt(line);
    return lineReadTemplate(path, callback, 1);
  }

  public String concentrate(String path) throws IOException {
    LineCallback<String> callback = (line, value) -> value + line;
    return lineReadTemplate(path, callback, "");
  }
}
