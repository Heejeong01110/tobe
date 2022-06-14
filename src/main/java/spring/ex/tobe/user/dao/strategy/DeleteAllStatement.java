package spring.ex.tobe.user.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import spring.ex.tobe.user.dao.strategy.StatementStrategy;

public class DeleteAllStatement implements StatementStrategy {

  @Override
  public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
    return c.prepareStatement("delete from users");
  }
}
