package spring.ex.tobe.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.ex.tobe.user.dao.Conncetion.ConnectionMaker;
import spring.ex.tobe.user.dao.Conncetion.CountingConnectionMaker;
import spring.ex.tobe.user.dao.Conncetion.DConnectionMaker;

@Configuration
public class CountingDaoFactory {

  @Bean
  public UserDao userDao() {
    UserDao userDao = new UserDao();
    userDao.setConnectionMaker(connectionMaker());
    return userDao;
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new CountingConnectionMaker(realConnectionMaker());
  }

  @Bean
  public ConnectionMaker realConnectionMaker() {
    return new DConnectionMaker();
  }
}