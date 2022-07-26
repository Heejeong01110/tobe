package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContextTest.xml")
@DirtiesContext
public class testDBTest {

  @Autowired
  private UserDao dao;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {
    //testDB를 코드를 통해 별도로 설정
    user1 = new User("aaaaa", "원", "password1", Level.BASIC, 1, 0);
    user2 = new User("ccccc", "투", "password2", Level.SILVER, 55, 10);
    user3 = new User("bbbbb", "쓰리", "password3", Level.GOLD, 100, 40);
  }

  @Test
  public void addAndGet() throws SQLException {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    User userFind1 = dao.get(user1.getId());
    assertThat(userFind1.getName(), is(user1.getName()));
    assertThat(userFind1.getPassword(), is(user1.getPassword()));

    User userFind2 = dao.get(user2.getId());
    assertThat(userFind2.getName(), is(user2.getName()));
    assertThat(userFind2.getPassword(), is(user2.getPassword()));
  }

  @Test
  public void count() throws SQLException {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    assertThat(dao.getCount(), is(1));

    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    dao.add(user3);
    assertThat(dao.getCount(), is(3));
  }

  @Test
  public void getUserFailure() throws SQLException {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
  }


}
