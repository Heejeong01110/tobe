package spring.ex.tobe.user.service;

import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

  @Nullable
  private String host;

  public void setHost(@Nullable String host) {
    this.host = host;
  }

  @Override
  public void send(SimpleMailMessage simpleMessage) throws MailException {

  }

  @Override
  public void send(SimpleMailMessage... simpleMessages) throws MailException {

  }
}
