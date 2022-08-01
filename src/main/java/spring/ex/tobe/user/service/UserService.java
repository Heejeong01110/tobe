package spring.ex.tobe.user.service;

import spring.ex.tobe.user.domain.User;

public interface UserService {

  void upgradeLevels() throws Exception;

  void add(User user);

}
