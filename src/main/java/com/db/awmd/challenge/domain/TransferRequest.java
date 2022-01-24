package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class TransferRequest {
	
	@NotNull
	@NotEmpty
	private String accountFromId;

	@NotNull
	@NotEmpty
	private String accountToId;

	@NotNull
	@Min(value = 0, message = "Transfer amount can not be less than zero")
	private BigDecimal amount;

	@JsonCreator
	public TransferRequest(@JsonProperty("accountFromId") String accountFromId,
			 @JsonProperty("accountToId") String accountToId,
			 @JsonProperty("amount") BigDecimal amount) {
		this.accountFromId = accountFromId;
		this.accountToId = accountToId;
		this.amount = amount;
	}
	

	
}
