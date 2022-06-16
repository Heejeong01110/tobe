package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.ex.tobe.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {//public 확인(JUnitCore)

  @Autowired
  private UserDao userDao;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {
    user1 = new User("aaaaa", "원", "password1");
    user2 = new User("ccccc", "투", "password2");
    user3 = new User("bbbbb", "쓰리", "password3");
  }

  @Test
  public void addAndGet() {
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
  public void count() {
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
  public void getUserFailure() {
    userDao.deleteAll();
    assertThat(userDao.getCount(), is(0));

    assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown_id"));
  }


  @Test
  void getAll() {
    userDao.deleteAll();
    List<User> users0 = userDao.getAll();
    assertThat(users0.size(), is(0));

    userDao.add(user1);
    List<User> users1 = userDao.getAll();
    checkSameUser(user1, users1.get(0));

    userDao.add(user2);
    List<User> users2 = userDao.getAll();
    assertThat(users2.size(), is(2));
    checkSameUser(user1, users2.get(0));
    checkSameUser(user2, users2.get(1));

    userDao.add(user3);
    List<User> users3 = userDao.getAll();
    assertThat(users3.size(), is(3));
    checkSameUser(user1, users3.get(0));
    checkSameUser(user3, users3.get(1));
    checkSameUser(user2, users3.get(2));
  }

  private void checkSameUser(User expect, User actual) {
    assertThat(expect.getId(), is(actual.getId()));
    assertThat(expect.getName(), is(actual.getName()));
    assertThat(expect.getPassword(), is(actual.getPassword()));

  }
}
