package com.db.awmd.challenge.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.TransferRequest;
import com.db.awmd.challenge.domain.TransferResult;
import com.db.awmd.challenge.exception.AccountNotExistException;
import com.db.awmd.challenge.exception.NotEnoughMoneyException;
import com.db.awmd.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/transferMoney")
@Slf4j
public class TransferController {
	
	private final AccountsService accountsService;

	@Autowired
	public TransferController(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> transferMoney(@RequestBody @Valid TransferRequest request) {

		try {
			TransferResult result= accountsService.transferMoney(request);
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (AccountNotExistException | NotEnoughMoneyException e) {
			log.error("Fail to transfer balances, please check with system administrator.");
			throw e;
		}

	}
}
