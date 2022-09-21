package spring.ex.tobe.user.sqlservice;

public interface SqlService {
  String getSql(String key) throws SqlRetrievalFalureException;
}
