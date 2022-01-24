package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferRequest;
import com.db.awmd.challenge.domain.TransferResult;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  @Test
  public void transferAmount() throws Exception{
	  Account accountFrom = new Account("1");
	  accountFrom.setBalance(new BigDecimal(1000));
	    this.accountsService.createAccount(accountFrom);
	    
	    Account accountTo = new Account("2");
	    accountTo.setBalance(new BigDecimal(2000));
		    this.accountsService.createAccount(accountTo);
		    
		    BigDecimal amount= new BigDecimal(100);
		    BigDecimal balance = accountFrom.getBalance();
		    TransferRequest request = new TransferRequest(accountFrom.getAccountId(),accountTo.getAccountId(),amount);   
		    TransferResult result= accountsService.transferMoney(request);
		    assertEquals(result.getBalanceAfterTransfer(), balance.subtract(amount));
  }
}
