package spring.ex.tobe.user.service;

import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import spring.ex.tobe.user.dao.UserDao;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserServiceImpl implements UserService {

  private UserDao userDao;
  private DataSource dataSource;
  private UserLevelUpgradePolicy userLevelUpgradePolicy;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
    this.userLevelUpgradePolicy = userLevelUpgradePolicy;
  }

  public void upgradeLevels() throws Exception {
    TransactionSynchronizationManager.initSynchronization();
    Connection c = DataSourceUtils.getConnection(dataSource);
    c.setAutoCommit(false);

    try {
      List<User> users = userDao.getAll();

      for (User user : users) {
        if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
          userLevelUpgradePolicy.upgradeLevel(user);
          userDao.update(user);
        }
      }
      c.commit();
    } catch (Exception e) {
      c.rollback();
      throw e;
    } finally {
      DataSourceUtils.releaseConnection(c, dataSource);
      TransactionSynchronizationManager.unbindResource(dataSource);
      TransactionSynchronizationManager.clearSynchronization();
    }


  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }

}
