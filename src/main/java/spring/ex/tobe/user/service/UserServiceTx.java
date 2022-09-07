package spring.ex.tobe.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.ex.tobe.user.domain.User;

public class UserServiceTx implements UserService {

  private PlatformTransactionManager transactionManager;
  private UserService userService;

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void upgradeLevels() throws Exception {
    TransactionStatus status =
        transactionManager.getTransaction(new DefaultTransactionDefinition());

    try {
      userService.upgradeLevels();
      transactionManager.commit(status);
    } catch (Exception e) {
      transactionManager.rollback(status);
      throw e;
    }
  }
  @Override
  public void add(User user) {
    userService.add(user);
  }
}
