package spring.ex.tobe.user.dao;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.ex.tobe.user.dao.Conncetion.CountingConnectionMaker;

public class UserDaoConnecionCountingTest {

  @Test
  public void main() throws SQLException, ClassNotFoundException {
    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao = context.getBean("userDao", UserDao.class);

    CountingConnectionMaker connectionMaker = context.getBean("conenctionMaker",
        CountingConnectionMaker.class);
    System.out.println("Connection counter : " + connectionMaker.getCount());
  }

}
