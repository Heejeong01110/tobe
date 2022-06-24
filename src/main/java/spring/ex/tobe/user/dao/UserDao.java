package spring.ex.tobe.user.dao;

import java.util.List;
import spring.ex.tobe.user.domain.User;

public interface UserDao {

  void add(User user);

  User get(String id);

  List<User> getAll();

  void deleteAll();

  int getCount();

}
