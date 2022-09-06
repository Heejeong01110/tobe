package spring.ex.tobe.user.service;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import spring.ex.tobe.user.dao.UserDao;
import spring.ex.tobe.user.domain.Level;
import spring.ex.tobe.user.domain.User;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {

  private static List<User> users;

  @Autowired
  private UserService userService;

  @Autowired
  private UserDao userDao;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Autowired
  private MailSender mailSender;

  @Autowired
  private UserLevelUpgradePolicy policy;

  @BeforeAll
  public static void setUpInit() {
    users = Arrays.asList(
        new User("aaaaa", "원", "password1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0,"aaaa@test.com"),
        new User("ccccc", "투", "password2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,
            0,"ccccc@test.com"),
        new User("bbbbb", "쓰리", "password3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
            MIN_RECOMMEND_FOR_GOLD - 1,"bbbbb@test.com"),
        new User("ddddd", "포", "password4", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
            MIN_RECOMMEND_FOR_GOLD,"ddddd@test.com"),
        new User("eeeee", "오", "password5", Level.GOLD, MIN_LOGCOUNT_FOR_SILVER + 1,
            Integer.MAX_VALUE,"eeeee@test.com")
    );
  }

  @BeforeEach
  public void setUp() {
    userDao.deleteAll();
  }

  @Test
  public void upgradeLevels() throws Exception {
    for (User user : users) {
      userDao.add(user);
    }

    userService.upgradeLevels();
    checkLevelUpgraded(users.get(0), false);
    checkLevelUpgraded(users.get(1), true);
    checkLevelUpgraded(users.get(2), false);
    checkLevelUpgraded(users.get(3), true);
    checkLevelUpgraded(users.get(4), false);
  }

  @Test
  public void upgradeLevelsMailMockTest(){
    for (User user : users) {
      userDao.add(user);
    }

    UserServiceImpl testUserService = new UserServiceImpl();
    MockMailSender mockMailSender = new MockMailSender();
    testUserService.setUserDao(userDao);
    testUserService.setTransactionManager(transactionManager);
    testUserService.setMailSender(mockMailSender);
    testUserService.setUserLevelUpgradePolicy(policy); //기존 DI 불러와서 넣기

    testUserService.upgradeLevels();
    checkLevelUpgraded(users.get(0), false);
    checkLevelUpgraded(users.get(1), true);
    checkLevelUpgraded(users.get(2), false);
    checkLevelUpgraded(users.get(3), true);
    checkLevelUpgraded(users.get(4), false);

    List<String> request = mockMailSender.getRequests();
    assertThat(request.size(), is(2));
    assertThat(request.get(0), is(users.get(1).getEmail()));
    assertThat(request.get(1), is(users.get(3).getEmail()));

  }

  @Test
  public void add() {
    User userWithLevel = users.get(4);
    User userWithoutLevel = users.get(0);
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
    assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));

  }

  @Test
  public void upgradeAllOrNothing(){
    for (User user : users) {
      userDao.add(user);
    }

    UserServiceImpl testUserService = new UserServiceImpl();
    testUserService.setUserDao(userDao);
    testUserService.setTransactionManager(transactionManager);
    testUserService.setMailSender(mailSender);
    testUserService.setUserLevelUpgradePolicy(new TestUserLevelUpgradePolicy(users.get(3).getId()));

    try {
      testUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {
      System.out.println("catch");
    }

    checkLevelUpgraded(users.get(1), false);
  }


  private void checkLevelUpgraded(User user, boolean upgraded) {
    User userUpdate = userDao.get(user.getId());
    if (upgraded) {
      assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
    } else {
      assertThat(userUpdate.getLevel(), is(user.getLevel()));
    }

  }
  static class MockMailSender implements MailSender{

    private List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
      return requests;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
      requests.add(simpleMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }
  }

  static class TestUserLevelUpgradePolicy extends OrdinaryUserLevelUpgradePolicy {

    private String id;

    private TestUserLevelUpgradePolicy(String id) {
      this.id = id;
    }

    @Override
    public void upgradeLevel(User user) {
      if (user.getId().equals(this.id)) {
        throw new TestUserServiceException();
      }
      super.upgradeLevel(user);
    }
  }

  static class TestUserServiceException extends RuntimeException {

  }

}
