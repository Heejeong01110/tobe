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

    User user = new User();
    user.setId("heejeong2");
    user.setName("희정");
    user.setPassword("password");

    dao.add(user);

    User user2 = dao.get(user.getId());

    assertThat(user2.getName(), is(user.getName()));
    assertThat(user2.getPassword(), is(user.getPassword()));
    assertThat(user2.getId(), is(user.getId()));
  }
}
