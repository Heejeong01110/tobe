package spring.ex.tobe.user.dao.Conncetion;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

  public Connection makeNewConnection() throws ClassNotFoundException, SQLException;
}
