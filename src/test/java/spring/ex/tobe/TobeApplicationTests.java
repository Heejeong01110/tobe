package spring.ex.tobe;

import org.junit.runner.JUnitCore;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TobeApplicationTests {

  public static void main(String[] args) {
    JUnitCore.main("spring.ex.tobe.user.dao.UserDaoTest");
  }

}
