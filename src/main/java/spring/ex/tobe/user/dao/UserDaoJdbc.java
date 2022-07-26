package spring.ex.tobe.user.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserDaoJdbc implements UserDao {

  private JdbcTemplate jdbcTemplate;
  private RowMapper<User> userMapper = (rs, rowNum) -> {
    User user = new User();
    user.setId(rs.getString("id"));
    user.setName(rs.getString("name"));
    user.setPassword(rs.getString("password"));
    user.setLevel(Level.valueOf(rs.getInt("level")));
    user.setLogin(rs.getInt("login"));
    user.setRecommend(rs.getInt("recommend"));
    return user;
  };

  public UserDaoJdbc() {
  }

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void add(final User user) {
    jdbcTemplate.update(
        "insert into users (id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)",
        user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(),
        user.getLogin(), user.getRecommend());
  }

  public User get(String id) {
    return jdbcTemplate.queryForObject("select * from users where id = ?",
        userMapper, id);
  }

  public List<User> getAll() {
    return jdbcTemplate.query("select * from users order by id", userMapper);
  }

  public void deleteAll() {
    jdbcTemplate.update("delete from users");
  }

  public int getCount() {
    return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
  }

  @Override
  public void update(User user) {
    jdbcTemplate.update(
        "update users set name = ?, password = ?, level = ?, login = ?, recommend = ? "
            + "where id = ?", user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
        user.getRecommend(), user.getId());
  }

}
