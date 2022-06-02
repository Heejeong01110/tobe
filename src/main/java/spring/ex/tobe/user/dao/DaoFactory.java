package spring.ex.tobe.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.ex.tobe.user.dao.Conncetion.CustomDataSource;

@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    UserDao userDao = new UserDao();
    userDao.setDataSource(dataSource());
    return userDao;
  }

  @Bean
  public AccountDao accountDao() {
    AccountDao accountDao = new AccountDao();
    accountDao.setDataSource(dataSource());
    return accountDao;
  }

  @Bean
  public DataSource dataSource() {
    return new CustomDataSource().getDataSource();
  }
}
