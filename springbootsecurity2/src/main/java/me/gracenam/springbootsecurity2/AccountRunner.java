package me.gracenam.springbootsecurity2;

import me.gracenam.springbootsecurity2.account.Account;
import me.gracenam.springbootsecurity2.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountRunner implements ApplicationRunner {

  @Autowired
  AccountService accountService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Account grace = accountService.createAccount("grace", "1234");
    System.out.println("grace.getName() = " + grace.getName());
    System.out.println("grace.getPassword() = " + grace.getPassword());
  }

}
