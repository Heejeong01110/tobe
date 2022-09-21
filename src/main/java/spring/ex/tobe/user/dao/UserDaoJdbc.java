package spring.ex.tobe.user.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserDaoJdbc implements UserDao {

  private String sqlAdd;
  private String sqlGet;
  private String sqlGetAll;
  private String sqlDeleteAll;
  private String sqlGetCount;
  private String sqlUpdate;

  private JdbcTemplate jdbcTemplate;

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

  public void setSqlAdd(String sqlAdd) {
    this.sqlAdd = sqlAdd;
  }

  public void setSqlGet(String sqlGet) {
    this.sqlGet = sqlGet;
  }

  public void setSqlGetAll(String sqlGetAll) {
    this.sqlGetAll = sqlGetAll;
  }

  public void setSqlDeleteAll(String sqlDeleteAll) {
    this.sqlDeleteAll = sqlDeleteAll;
  }

  public void setSqlGetCount(String sqlGetCount) {
    this.sqlGetCount = sqlGetCount;
  }

  public void setSqlUpdate(String sqlUpdate) {
    this.sqlUpdate = sqlUpdate;
  }

  public void add(final User user) {
    jdbcTemplate.update(
        sqlAdd,
        user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(),
        user.getLogin(), user.getRecommend(), user.getEmail());
  }

  public User get(String id) {
    return jdbcTemplate.queryForObject(sqlGet,
        userMapper, id);
  }

  public List<User> getAll() {
    return jdbcTemplate.query(sqlGetAll, userMapper);
  }

  public void deleteAll() {
    jdbcTemplate.update(sqlDeleteAll);
  }

  public int getCount() {
    return jdbcTemplate.queryForObject(sqlGetCount, Integer.class);
  }

  @Override
  public void update(User user) {
    jdbcTemplate.update(sqlUpdate, user.getName(), user.getPassword(), user.getLevel().intValue(),
        user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
  }

}
