package spring.ex.tobe.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
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
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost/tobe");
    dataSource.setUsername("spring");
    dataSource.setPassword("root1234");
    return dataSource;
  }
}
