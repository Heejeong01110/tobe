package spring.ex.tobe.user.service;

import spring.ex.tobe.user.domain.User;

public interface UserLevelUpgradePolicy {
  boolean canUpgradeLevel(User user);
}
