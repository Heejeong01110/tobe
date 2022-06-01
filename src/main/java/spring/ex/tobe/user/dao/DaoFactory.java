package spring.ex.tobe.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.ex.tobe.user.dao.Conncetion.ConnectionMaker;
import spring.ex.tobe.user.dao.Conncetion.DConnectionMaker;

@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    UserDao userDao = new UserDao();
    userDao.setConnectionMaker(connectionMaker());
    return userDao;
  }

  @Bean
  public AccountDao accountDao() {
    return new AccountDao(connectionMaker());
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new DConnectionMaker();
  }
}
