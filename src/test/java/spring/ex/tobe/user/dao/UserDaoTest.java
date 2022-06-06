package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;//라이브러리 확인(JUnitCore)
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import org.junit.jupiter.api.Test; //class에서 실행할 경우
//import org.junit.Test;//라이브러리 확인(JUnitCore)
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import spring.ex.tobe.user.domain.User;

public class UserDaoTest {//public 확인(JUnitCore)

  private UserDao userDao;

  @BeforeEach
  public void setUp(){
    ApplicationContext context = new GenericXmlApplicationContext(
        "applicationContext.xml");
    userDao = context.getBean("userDao", UserDao.class);
  }

  @Test
  public void addAndGet() throws SQLException {
    userDao.deleteAll();
    assertThat(userDao.getCount(), is(0));

    User user1 = new User("one", "원", "password1");
    User user2 = new User("two", "투", "password2");

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
    User user1 = new User("one", "원", "password1");
    User user2 = new User("two", "투", "password2");
    User user3 = new User("three", "쓰리", "password3");

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
