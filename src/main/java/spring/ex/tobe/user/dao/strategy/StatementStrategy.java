package spring.ex.tobe.user.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {

  PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
