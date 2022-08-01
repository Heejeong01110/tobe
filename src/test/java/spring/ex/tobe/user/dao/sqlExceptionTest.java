package spring.ex.tobe.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContextTest.xml")
public class sqlExceptionTest {

  @Autowired
  private UserDao dao;

  @Autowired
  private DataSource dataSource;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  public void setUp() {
    user1 = new User("aaaaa", "원", "password1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0,"aaaa@test.com");
    user2 = new User("bbbbb", "쓰리", "password3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
        MIN_RECOMMEND_FOR_GOLD - 1,"bbbbb@test.com");
    user3 =new User("bbbbb", "쓰리", "password3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
        MIN_RECOMMEND_FOR_GOLD - 1,"bbbbb@test.com");

    dao.deleteAll();
  }

  @Test
  public void sqlExceptionTranslate() {
    try {
      dao.add(user1);
      dao.add(user1);
    } catch (DuplicateKeyException e) {
      SQLException sqlEx = (SQLException) e.getRootCause();
      SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(dataSource);

      assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
    }
  }
}
