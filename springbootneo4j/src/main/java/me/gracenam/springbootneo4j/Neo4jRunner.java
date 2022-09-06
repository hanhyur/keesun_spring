package me.gracenam.springbootneo4j;

import me.gracenam.springbootneo4j.account.Account;
import me.gracenam.springbootneo4j.account.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Neo4jRunner implements ApplicationRunner {

  @Autowired
  Neo4jTemplate neo4jTemplate;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Account account = new Account();
    account.setEmail("grace@email.com");
    account.setUsername("grace");

    Role role = new Role();
    role.setName("admin");

    account.getRoles().add(role);

    neo4jTemplate.save(account);

    System.out.println("finished");
  }
}
