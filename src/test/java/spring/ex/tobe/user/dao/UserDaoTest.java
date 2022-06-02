package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;//라이브러리 확인(JUnitCore)

import java.sql.SQLException;
import org.junit.jupiter.api.Test; //class에서 실행할 경우
//import org.junit.Test;//라이브러리 확인(JUnitCore)
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.ex.tobe.user.domain.User;

public class UserDaoTest {//public 확인(JUnitCore)


  @Test
  public void addAndGet() throws SQLException {
    ApplicationContext context = new GenericXmlApplicationContext(
        "applicationContext.xml");
    UserDao dao = context.getBean("userDao", UserDao.class);

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    User user1 = new User("one","원","password1");
    User user2 = new User("two","투","password2");

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
  public void count() throws SQLException{
    ApplicationContext context = new GenericXmlApplicationContext(
        "applicationContext.xml");
    UserDao dao = context.getBean("userDao", UserDao.class);

    User user1 = new User("one","원","password1");
    User user2 = new User("two","투","password2");
    User user3 = new User("three","쓰리","password3");

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    assertThat(dao.getCount(), is(1));

    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    dao.add(user3);
    assertThat(dao.getCount(), is(3));



  }


}
