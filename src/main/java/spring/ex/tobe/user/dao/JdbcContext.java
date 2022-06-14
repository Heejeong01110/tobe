package spring.ex.tobe.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.sql.DataSource;

public class JdbcContext {

  private DataSource dataSource;

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
    Connection c = null;
    PreparedStatement ps = null;
    try {
      c = dataSource.getConnection();
      ps = stmt.makePreparedStatement(c);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw e;
    } finally {
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
        }
      }
      if (c != null) {
        try {
          c.close();
        } catch (SQLException e) {
        }
      }
    }
  }


  public void excuteSql(final String query) throws SQLException {
    workWithStatementStrategy(c -> c.prepareStatement(query));
  }

  public void excuteSql(final String query, String...infos) throws SQLException {
    workWithStatementStrategy(c -> {
      PreparedStatement ps = c.prepareStatement(query);
      for(int i=0;i<infos.length;i++){
        ps.setString(i+1, infos[i]);
      }
      return ps;
    });
  }
}
