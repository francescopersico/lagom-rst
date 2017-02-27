package cz.codinmonkey.ibs.account.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Wither;

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
@Wither
public class Account {

	private final String iban;
	private final BigDecimal balance;

	@JsonCreator
	public Account(String iban, BigDecimal balance) {
		this.iban = iban;
		this.balance = balance;
	}
}
