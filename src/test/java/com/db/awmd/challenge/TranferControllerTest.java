package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferRequest;
import com.db.awmd.challenge.domain.TransferResult;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TranferControllerTest {

	  private MockMvc mockMvc;

	  @Autowired
	  private AccountsService accountsService;

	  @Autowired
	  private WebApplicationContext webApplicationContext;
	  
	  @Before
	  public void prepareMockMvc() {
	    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

	    // Reset the existing accounts before each test.
	    accountsService.getAccountsRepository().clearAccounts();
	  }
	  @Test
	  public void transferMoney() throws Exception{
		  this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
			      .content("{\"accountId\":\"1\",\"balance\":1000}")).andExpect(status().isCreated());
		  this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
			      .content("{\"accountId\":\"2\",\"balance\":2000}")).andExpect(status().isCreated());
		  Account accountFrom = accountsService.getAccount("1");
		  Account accountTo= accountsService.getAccount("2");
		  BigDecimal balance = accountFrom.getBalance();
		  BigDecimal amount= new BigDecimal(100);
		  TransferRequest request = new TransferRequest(accountFrom.getAccountId(),accountTo.getAccountId(),amount);
		  TransferResult result= accountsService.transferMoney(request);
		  assertEquals(result.getBalanceAfterTransfer(), balance.subtract(amount));
	  }
}
