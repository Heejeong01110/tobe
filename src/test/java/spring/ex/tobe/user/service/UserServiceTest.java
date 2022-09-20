package spring.ex.tobe.user.service;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
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
  private UserService testUserService;

  @Autowired
  private UserDao userDao;

  @Autowired
  private UserLevelUpgradePolicy policy;

  @BeforeAll
  public static void setUpInit() {
    users = Arrays.asList(
        new User("aaaaa", "원", "password1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0,
            "aaaa@test.com"),
        new User("ccccc", "투", "password2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,
            0, "ccccc@test.com"),
        new User("bbbbb", "쓰리", "password3", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
            MIN_RECOMMEND_FOR_GOLD - 1, "bbbbb@test.com"),
        new User("ddddd", "포", "password4", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER + 1,
            MIN_RECOMMEND_FOR_GOLD, "ddddd@test.com"),
        new User("eeeee", "오", "password5", Level.GOLD, MIN_LOGCOUNT_FOR_SILVER + 1,
            Integer.MAX_VALUE, "eeeee@test.com")
    );
  }

  @BeforeEach
  public void setUp() {
    userDao.deleteAll();
  }

  @Test
  public void upgradeLevels() {
    UserServiceImpl userServiceImpl = new UserServiceImpl();
    UserDao mockUserDao = mock(UserDao.class);
    MailSender mockMailSender = mock(MailSender.class);
    ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(
        SimpleMailMessage.class);

    userServiceImpl.setUserDao(mockUserDao);
    userServiceImpl.setMailSender(mockMailSender);
    userServiceImpl.setUserLevelUpgradePolicy(policy);

    when(mockUserDao.getAll()).thenReturn(users);

    userServiceImpl.upgradeLevels();

    verify(mockUserDao, times(2)).update(any(User.class));

    verify(mockUserDao).update(users.get(1));
    assertThat(users.get(1).getLevel(), is(Level.SILVER));
    verify(mockUserDao).update(users.get(3));
    assertThat(users.get(3).getLevel(), is(Level.GOLD));

    verify(mockMailSender, times(2)).send(mailMessageArg.capture());
    List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
    assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
    assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));

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
  public void upgradeAllOrNothingProxy() {
    for (User user : users) {
      userDao.add(user);
    }

    try {

      this.testUserService.upgradeLevels();

      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {
      System.out.println("catch");
    } catch (Exception e) {
      e.printStackTrace();
    }

    checkLevelUpgraded(users.get(1), false);
  }

  @Test
  public void readOnlyTransactionAttribute() {
    for (User user : users) {
      userDao.add(user);
    }

    assertThrows(TransientDataAccessResourceException.class, () -> testUserService.getAll());
  }

  private void checkLevelUpgraded(User user, boolean upgraded) {
    User userUpdate = userDao.get(user.getId());
    if (upgraded) {
      assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
    } else {
      assertThat(userUpdate.getLevel(), is(user.getLevel()));
    }

  }

  static class TestUserServiceException extends RuntimeException {

  }

  @Transactional
  static class TestUserService extends UserServiceImpl {

    private String id = "ddddd";

    protected void upgradeLevel(User user) {
      if (user.getId().equals(this.id)) {
        throw new TestUserServiceException();
      }
      super.upgradeLevel(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
      List<User> users = super.getAll();
      for (User user : users) {
        super.update(user);
      }
      return null;
    }

    @Override
    @Transactional(readOnly = true)
    public User get(String id) {
      return super.get(id);
    }
  }

}
