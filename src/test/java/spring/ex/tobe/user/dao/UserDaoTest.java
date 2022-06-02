package spring.ex.tobe.user.dao;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.ex.tobe.user.domain.User;

class UserDaoTest {

  @Test
  public void main() throws SQLException, ClassNotFoundException {
//    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

    GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
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

  @Test
  public void daoTest() {
    DaoFactory factory = new DaoFactory();
    UserDao dao1 = factory.userDao();
    UserDao dao2 = factory.userDao();

    System.out.println(dao1); //다른 값 출력
    System.out.println(dao2);

    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao3 = context.getBean("userDao", UserDao.class);
    UserDao dao4 = context.getBean("userDao", UserDao.class);

    System.out.println(dao3); //같은 값 출력
    System.out.println(dao4);
  }
}
