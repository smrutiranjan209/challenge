package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferRequest;
import com.db.awmd.challenge.domain.TransferResult;
import com.db.awmd.challenge.exception.AccountNotExistException;
import com.db.awmd.challenge.exception.NotEnoughMoneyException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	public TransferResult transferMoney(TransferRequest request) {
		TransferResult result = new TransferResult();
		Account accountFrom = accountsRepository.getAccount(request.getAccountFromId());

		Account accountTo = accountsRepository.getAccount(request.getAccountToId());

		if (accountFrom == null || accountTo == null) {
			throw new AccountNotExistException("Account " + accountFrom.getAccountId() + " does not exist.");
		} else if (accountFrom.getBalance().compareTo(request.getAmount()) < 0) {
			throw new NotEnoughMoneyException(
					"Account with id:" + accountFrom.getAccountId() + " does not have enough balance to transfer.");
		} else {
			accountFrom.setBalance(accountFrom.getBalance().subtract(request.getAmount()));
			accountTo.setBalance(accountTo.getBalance().add(request.getAmount()));
		}
		result.setAccountFromId(accountFrom.getAccountId());
		result.setBalanceAfterTransfer(accountFrom.getBalance());
		return result;
	}
	
}
