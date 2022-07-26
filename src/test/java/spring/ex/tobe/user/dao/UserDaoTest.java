package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {//public 확인(JUnitCore)

  @Autowired
  private UserDao dao;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {
    user1 = new User("aaaaa", "원", "password1", Level.BASIC, 1, 0);
    user2 = new User("ccccc", "투", "password2", Level.SILVER, 55, 10);
    user3 = new User("bbbbb", "쓰리", "password3", Level.GOLD, 100, 40);

    dao.deleteAll();
  }

  @Test
  public void addAndGet() {
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    User userFind1 = dao.get(user1.getId());
    checkSameUser(userFind1, user1);

    User userFind2 = dao.get(user2.getId());
    checkSameUser(userFind2, user2);
  }

  @Test
  public void count() {
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    assertThat(dao.getCount(), is(1));

    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    dao.add(user3);
    assertThat(dao.getCount(), is(3));
  }

  @Test
  public void getUserFailure() {
    assertThat(dao.getCount(), is(0));

    assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
  }

  @Test
  public void duplicateKey() {
    dao.add(user1);
    assertThrows(DuplicateKeyException.class, () -> dao.add(user1));
  }


  @Test
  void getAll() {
    List<User> users0 = dao.getAll();
    assertThat(users0.size(), is(0));

    dao.add(user1);
    List<User> users1 = dao.getAll();
    checkSameUser(user1, users1.get(0));

    dao.add(user2);
    List<User> users2 = dao.getAll();
    assertThat(users2.size(), is(2));
    checkSameUser(user1, users2.get(0));
    checkSameUser(user2, users2.get(1));

    dao.add(user3);
    List<User> users3 = dao.getAll();
    assertThat(users3.size(), is(3));
    checkSameUser(user1, users3.get(0));
    checkSameUser(user3, users3.get(1));
    checkSameUser(user2, users3.get(2));
  }

  @Test
  void update() {
    dao.add(user1);

    user1.setName("수정01");
    user1.setPassword("modify01");
    user1.setLevel(Level.SILVER);
    user1.setLogin(1000);
    user1.setRecommend(999);
    dao.update(user1);

    User user1Update = dao.get(user1.getId());
    checkSameUser(user1, user1Update);
  }

  private void checkSameUser(User expect, User actual) {
    assertThat(expect.getId(), is(actual.getId()));
    assertThat(expect.getName(), is(actual.getName()));
    assertThat(expect.getPassword(), is(actual.getPassword()));
    assertThat(expect.getLevel(), is(actual.getLevel()));
    assertThat(expect.getLogin(), is(actual.getLogin()));
    assertThat(expect.getRecommend(), is(actual.getRecommend()));

  }
}
