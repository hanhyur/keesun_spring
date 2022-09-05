package me.gracenam.springbootmongo.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.4.2")
class AccountRepositoryTest {

  @Autowired
  AccountRepository accountRepository;

  @Test
  public void findByEmail() {
    Account account = new Account();
    account.setUsername("gracenam");
    account.setEmail("gracenam@email.com");

    accountRepository.save(account);

    Optional<Account> byId = accountRepository.findById(account.getId());
    assertThat(byId).isNotEmpty();

    Optional<Account> byEmail = accountRepository.findByEmail(account.getEmail());
    assertThat(byEmail).isNotEmpty();
    assertThat(byEmail.get().getUsername()).isEqualTo("gracenam");
  }

}