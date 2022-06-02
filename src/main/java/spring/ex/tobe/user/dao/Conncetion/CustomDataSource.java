package spring.ex.tobe.user.dao.Conncetion;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class CustomDataSource {

  public DataSource getDataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost/tobe");
    dataSource.setUsername("spring");
    dataSource.setPassword("root1234");
    return dataSource;
  }
}
