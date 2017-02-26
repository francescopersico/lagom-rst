package cz.codinmonkey.ibs.account.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;

/**
 * @author Richard Stefanca
 */

@Immutable
@JsonDeserialize
@ToString
@EqualsAndHashCode
@Builder
@Getter
public class Deposit {

	private final String otherIban;
	private final BigDecimal amount;

	@JsonCreator
	public Deposit(String otherIban, BigDecimal amount) {
		this.otherIban = otherIban;
		this.amount = amount;
	}
}
