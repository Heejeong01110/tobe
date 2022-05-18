package spring.ex.tobe.user.dao;

import spring.ex.tobe.user.dao.Conncetion.ConnectionMaker;
import spring.ex.tobe.user.dao.Conncetion.NConnectionMaker;

public class DaoFactory {
  public UserDao userDao(){
    ConnectionMaker connectionMaker = new NConnectionMaker();
    return new UserDao(connectionMaker);
  }
}
