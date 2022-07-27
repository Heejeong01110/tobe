package spring.ex.tobe.user.service;

import java.text.MessageFormat;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class OrdinaryUserLevelUpgradePolicy implements UserLevelUpgradePolicy{

  public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
  public static final int MIN_RECOMMEND_FOR_GOLD = 30;

  @Override
  public boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC : return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
      case SILVER : return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
      case GOLD : return false;
      default : throw new IllegalArgumentException(
          MessageFormat.format("Unknown Level: {0}", currentLevel));
    }
  }
}

