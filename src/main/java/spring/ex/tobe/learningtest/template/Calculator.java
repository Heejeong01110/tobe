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

  public int calcSum(String path) throws IOException {
    BufferedReaderCallback callback = br -> {
      Integer sum = 0;
      String line = null;
      while ((line = br.readLine()) != null) {
        sum += Integer.parseInt(line);
      }
      return sum;
    };
    return fileReadTemplate(path, callback);
  }

  public int calcMultiply(String path) throws IOException {
    BufferedReaderCallback callback = br -> {
      Integer sum = 1;
      String line = null;
      while ((line = br.readLine()) != null) {
        sum *= Integer.parseInt(line);
      }
      return sum;
    };
    return fileReadTemplate(path, callback);
  }
}
