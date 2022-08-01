package spring.ex.tobe.user.service;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.ex.tobe.user.dao.UserDao;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserServiceImpl implements UserService {

  private UserDao userDao;
  private PlatformTransactionManager transactionManager;
  private UserLevelUpgradePolicy userLevelUpgradePolicy;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
    this.userLevelUpgradePolicy = userLevelUpgradePolicy;
  }

  public void upgradeLevels() {
    TransactionStatus status =
        transactionManager.getTransaction(new DefaultTransactionDefinition());

    try {
      List<User> users = userDao.getAll();

      for (User user : users) {
        if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
          upgradeLevel(user);
        }
      }
      transactionManager.commit(status);
    } catch (Exception e) {
      transactionManager.rollback(status);
      throw e;
    }
  }

  private void upgradeLevel(User user){
    userLevelUpgradePolicy.upgradeLevel(user);
    userDao.update(user);
    sendUpgradeEmail(user);
  }

  private void sendUpgradeEmail(User user) {
    Properties props = new Properties();
    props.put("mail.stmp.host", "mail.ksug.org");
    Session s = Session.getInstance(props, null);

    MimeMessage message = new MimeMessage(s);
    try{
      message.setFrom(new InternetAddress("admin@test.com"));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
      message.setSubject("Upgrade 안내");
      message.setText("사용자님의 등급이 "+user.getLevel().name()+"로 업그레이드 되었습니다.");

      Transport.send(message);
    } catch (AddressException e) {
      throw new RuntimeException(e);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }

  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }

}
