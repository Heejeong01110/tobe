package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.ex.tobe.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DirtiesContext
public class testDBTest {

  @Autowired
  private UserDao userDao;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {
    //testDB를 코드를 통해 별도로 설정
    DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/testDB",
        "spring", "root1234", true);
    userDao.setDataSource(dataSource);
    user1 = new User("one", "원", "password1");
    user2 = new User("two", "투", "password2");
    user3 = new User("three", "쓰리", "password3");
  }

  @Test
  public void addAndGet() throws SQLException {
    userDao.deleteAll();
    assertThat(userDao.getCount(), is(0));

    userDao.add(user1);
    userDao.add(user2);
    assertThat(userDao.getCount(), is(2));

    User userFind1 = userDao.get(user1.getId());
    assertThat(userFind1.getName(), is(user1.getName()));
    assertThat(userFind1.getPassword(), is(user1.getPassword()));

    User userFind2 = userDao.get(user2.getId());
    assertThat(userFind2.getName(), is(user2.getName()));
    assertThat(userFind2.getPassword(), is(user2.getPassword()));
  }

  @Test
  public void count() throws SQLException {
    userDao.deleteAll();
    assertThat(userDao.getCount(), is(0));

    userDao.add(user1);
    assertThat(userDao.getCount(), is(1));

    userDao.add(user2);
    assertThat(userDao.getCount(), is(2));

    userDao.add(user3);
    assertThat(userDao.getCount(), is(3));
  }

  @Test
  public void getUserFailure() throws SQLException {
    userDao.deleteAll();
    assertThat(userDao.getCount(), is(0));

    assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown_id"));
  }


}
