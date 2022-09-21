package spring.ex.tobe.user.dao;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;
import spring.ex.tobe.user.sqlservice.SqlService;

public class UserDaoJdbc implements UserDao {

  private Map<String, String> sqlMap;
  private JdbcTemplate jdbcTemplate;
  private SqlService sqlService;

  private RowMapper<User> userMapper = (rs, rowNum) -> {
    User user = new User();
    user.setId(rs.getString("id"));
    user.setName(rs.getString("name"));
    user.setPassword(rs.getString("password"));
    user.setLevel(Level.valueOf(rs.getInt("level")));
    user.setLogin(rs.getInt("login"));
    user.setRecommend(rs.getInt("recommend"));
    user.setEmail(rs.getString("email"));
    return user;
  };

  public UserDaoJdbc() {
  }

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void setSqlMap(Map<String, String> sqlMap) {
    this.sqlMap = sqlMap;
  }

  public void setSqlService(SqlService sqlService) {
    this.sqlService = sqlService;
  }

  public void add(final User user) {
    jdbcTemplate.update(
        sqlService.getSql("add"),
        user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(),
        user.getLogin(), user.getRecommend(), user.getEmail());
  }

  public User get(String id) {
    return jdbcTemplate.queryForObject(sqlService.getSql("get"),
        userMapper, id);
  }

  public List<User> getAll() {
    return jdbcTemplate.query(sqlService.getSql("getAll"), userMapper);
  }

  public void deleteAll() {
    jdbcTemplate.update(sqlService.getSql("deleteAll"));
  }

  public int getCount() {
    return jdbcTemplate.queryForObject(sqlService.getSql("getCount"), Integer.class);
  }

  @Override
  public void update(User user) {
    jdbcTemplate.update(sqlService.getSql("update"), user.getName(), user.getPassword(),
        user.getLevel().intValue(),
        user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
  }

}
