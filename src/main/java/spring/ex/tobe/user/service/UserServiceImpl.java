package spring.ex.tobe.user.service;

import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.ex.tobe.user.dao.UserDao;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserServiceImpl implements UserService {

  private UserDao userDao;
  private PlatformTransactionManager transactionManager;
  private UserLevelUpgradePolicy userLevelUpgradePolicy;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
    this.userLevelUpgradePolicy = userLevelUpgradePolicy;
  }

  public void upgradeLevels() {
    TransactionStatus status =
        transactionManager.getTransaction(new DefaultTransactionDefinition());

    try {
      List<User> users = userDao.getAll();

      for (User user : users) {
        if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
          userLevelUpgradePolicy.upgradeLevel(user);
          userDao.update(user);
        }
      }
      transactionManager.commit(status);
    } catch (Exception e) {
      transactionManager.rollback(status);
      throw e;
    }
  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }

}
