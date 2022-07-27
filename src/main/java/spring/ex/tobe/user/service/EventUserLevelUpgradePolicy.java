package spring.ex.tobe.user.service;

import java.text.MessageFormat;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class EventUserLevelUpgradePolicy implements UserLevelUpgradePolicy {
  public static final int EVENT_MIN_LOGCOUNT_FOR_SILVER = 20;
  public static final int EVENT_RECOMMEND_FOR_GOLD = 10;

  @Override
  public boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC : return user.getLogin() >= EVENT_MIN_LOGCOUNT_FOR_SILVER;
      case SILVER : return user.getRecommend() >= EVENT_RECOMMEND_FOR_GOLD;
      case GOLD : return false;
      default : throw new IllegalArgumentException(
          MessageFormat.format("Unknown Level: {0}", currentLevel));
    }
  }
}
