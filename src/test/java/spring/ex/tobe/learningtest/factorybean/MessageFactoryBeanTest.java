package spring.ex.tobe.learningtest.factorybean;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/MessageFactoryBeanTest-context.xml")
public class MessageFactoryBeanTest {

  @Autowired
  private ApplicationContext context;

  @Test
  public void getMessageFromFactoryBean() {
    Object message = context.getBean("message");
    assertThat(message.getClass(), is(Message.class));
    assertThat(((Message)message).getText(), is("Factory Bean"));
  }

  @Test
  public void getFactoryBean(){
    Object factory = context.getBean("&message");
    assertThat(factory.getClass(), is(MessageFactoryBean.class));
  }

}
