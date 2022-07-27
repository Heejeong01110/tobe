package spring.ex.tobe.user.service;

import java.util.List;
import spring.ex.tobe.user.dao.UserDao;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserService {

  UserDao userDao;
  UserLevelUpgradePolicy userLevelUpgradePolicy;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
    this.userLevelUpgradePolicy = userLevelUpgradePolicy;
  }

  public void upgradeLevels() {
    List<User> users = userDao.getAll();

    for (User user : users) {
      if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
  }

  public void upgradeLevel(User user) {
    user.upgradeLevel();
    userDao.update(user);
  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }

}
