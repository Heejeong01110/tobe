package spring.ex.tobe.user.dao;

import spring.ex.tobe.user.dao.Conncetion.ConnectionMaker;
import spring.ex.tobe.user.dao.Conncetion.DConnectionMaker;

public class DaoFactory {
  public UserDao userDao(){
    return new UserDao(connectionMaker());
  }

  public AccountDao accountDao(){
    return new AccountDao(connectionMaker());
  }

  public ConnectionMaker connectionMaker(){
    return new DConnectionMaker();
  }
}
