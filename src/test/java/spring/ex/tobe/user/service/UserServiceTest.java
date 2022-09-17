package spring.ex.tobe.user.service;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static spring.ex.tobe.user.service.OrdinaryUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
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
  private ApplicationContext context;

  @Autowired
  private UserServiceImpl userServiceImpl;

  @Autowired
  private UserService userService;

  @Autowired
  private UserDao userDao;

  @Autowired
  private PlatformTransactionManager transactionManager;

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
  @DirtiesContext
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
  @DirtiesContext
  public void upgradeAllOrNothing() {
    for (User user : users) {
      userDao.add(user);
    }

    userServiceImpl.setUserLevelUpgradePolicy(
        new TestUserLevelUpgradePolicy(users.get(3).getId()));//기존 DI 불러와서 넣기

    //bean 이름을 userService 로 하는 경우 팩토리빈의 오브젝트 타입을 돌려주고
    //&를 붙였을 경우 팩토리빈 자체를 돌려준다
    ProxyFactoryBean proxyFactoryBean = context.getBean("&userService",
        ProxyFactoryBean.class); //팩토리 빈 자체를 가져와아하므로 빈 이름에 & 넣기
    proxyFactoryBean.setTarget(userServiceImpl);
    UserService txUserService = (UserService) proxyFactoryBean.getObject();

    try {
      txUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {
      System.out.println("catch");
    } catch (Exception e) {
      e.printStackTrace();
    }

    checkLevelUpgraded(users.get(1), false);
  }


  @Test
  @DirtiesContext
  public void upgradeAllOrNothingProxy() {

    TransactionHandler txHandler = new TransactionHandler();
    txHandler.setTarget(userServiceImpl);
    txHandler.setTransactionManager(transactionManager);
    txHandler.setPattern("get");

    UserService proxyUserService = (UserService) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class[]{UserService.class},
        txHandler
    );

    for (User user : users) {
      userDao.add(user);
    }

    userServiceImpl.setUserLevelUpgradePolicy(
        new TestUserLevelUpgradePolicy(users.get(3).getId()));//기존 DI 불러와서 넣기
    try {

      proxyUserService.upgradeLevels();

      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {
      System.out.println("catch");
    } catch (Exception e) {
      e.printStackTrace();
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

  static class TestUserLevelUpgradePolicy extends OrdinaryUserLevelUpgradePolicy {

    private final String id;

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
