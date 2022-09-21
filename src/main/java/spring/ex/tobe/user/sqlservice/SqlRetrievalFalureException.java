package spring.ex.tobe.user.sqlservice;

public class SqlRetrievalFalureException extends RuntimeException {

  public SqlRetrievalFalureException(String message, Throwable cause) {
    super(message, cause);
  }

  public SqlRetrievalFalureException(String message) {
    super(message);

  }
}
