package spring.ex.tobe.user.service;

import java.util.List;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import spring.ex.tobe.user.dao.UserDao;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;

public class UserServiceImpl implements UserService {

  private UserDao userDao;
  private UserLevelUpgradePolicy userLevelUpgradePolicy;
  private MailSender mailSender;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }


  public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
    this.userLevelUpgradePolicy = userLevelUpgradePolicy;
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void upgradeLevels() {
    List<User> users = userDao.getAll();

    for (User user : users) {
      if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
  }

  private void upgradeLevel(User user){
    userLevelUpgradePolicy.upgradeLevel(user);
    userDao.update(user);
    sendUpgradeEmail(user);
  }

  private void sendUpgradeEmail(User user) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(user.getEmail());
    mailMessage.setFrom("admin@test.com");
    mailMessage.setSubject("Upgrade 안내");
    mailMessage.setText("사용자님의 등급이 "+user.getLevel().name()+"로 업그레이드 되었습니다.");

    mailSender.send(mailMessage);
  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }

}
