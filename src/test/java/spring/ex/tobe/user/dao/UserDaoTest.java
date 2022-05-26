package spring.ex.tobe.user.dao;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.ex.tobe.user.domain.User;

class UserDaoTest {

  @Test
  public void main() throws SQLException, ClassNotFoundException {
    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao = context.getBean("userDao", UserDao.class);
//    UserDao dao = new DaoFactory().userDao();

    User user = new User();
    user.setId("heejeong2");
    user.setName("희정");
    user.setPassword("password");

    dao.add(user);

    System.out.println(user.getId() + " 등록 성공");

    User user2 = dao.get(user.getId());
    System.out.println(user2.getName());
    System.out.println(user2.getPassword());
  }
}
