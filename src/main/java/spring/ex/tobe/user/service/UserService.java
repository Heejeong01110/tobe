package spring.ex.tobe.user.service;

import java.text.MessageFormat;
import java.util.List;
import spring.ex.tobe.user.dao.UserDao;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserService {

  UserDao userDao;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
      if (canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }

  }

  private void upgradeLevel(User user) {
    user.upgradeLevel();
    userDao.update(user);
  }

  private boolean canUpgradeLevel(User user) {
    boolean canUpgrade = false;
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC -> canUpgrade = user.getLogin() >= 50;
      case SILVER -> canUpgrade = user.getRecommend() >= 30;
      case GOLD -> canUpgrade = false;
      default -> throw new IllegalArgumentException(
          MessageFormat.format("unknown Level: {0}", currentLevel));
    }
    return canUpgrade;
  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }
}
