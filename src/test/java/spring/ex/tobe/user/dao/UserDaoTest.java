package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
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

  private static User user1;
  private static User user2;
  private static User user3;

  @Autowired
  private UserDao dao;

  @BeforeAll
  public static void setUpInit() {
    user1 = new User("aaaaa", "원", "password1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0,
        "aaaa@test.com");
    user2 = new User("bbbbb", "투", "password2", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
        MIN_RECOMMEND_FOR_GOLD - 1, "bbbbb@test.com");
    user3 = new User("ccccc", "쓰리", "password3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
        MIN_RECOMMEND_FOR_GOLD - 1, "ccccc@test.com");
  }

  @BeforeEach
  public void setUp() {
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
    checkSameUser(user2, users3.get(1));
    checkSameUser(user3, users3.get(2));
  }

  @Test
  void update() {
    dao.add(user1);
    dao.add(user2);

    user1.setName("수정01");
    user1.setPassword("modify01");
    user1.setLevel(Level.SILVER);
    user1.setLogin(1000);
    user1.setRecommend(999);
    dao.update(user1);

    User user1Update = dao.get(user1.getId());
    checkSameUser(user1, user1Update);
    //update문에서 where절이 없을 경우에도 정상적으로 실행되는 예외를 방지하기위해 추가로 테스트
    User user2Update = dao.get(user2.getId());
    checkSameUser(user2, user2Update);
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
