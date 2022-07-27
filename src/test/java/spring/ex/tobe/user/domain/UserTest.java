package spring.ex.tobe.user.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

  User user;

  @BeforeEach
  void setUp() {
    user = new User();
  }

  @Test
  void upgradeLevel() {
    Level[] levels = Level.values();
    for(Level level : levels){
      if(level.nextLevel() == null) continue;
      user.setLevel(level);
      user.upgradeLevel();
      assertThat(user.getLevel(), is(level.nextLevel()));
    }
  }


  @Test
  void cannotUpgradeLevel(){
    Level[] levels = Level.values();
    for(Level level : levels){
      if(level.nextLevel() != null) continue;
      user.setLevel(level);
      assertThrows(IllegalArgumentException.class, () -> user.upgradeLevel());
    }
  }
}
