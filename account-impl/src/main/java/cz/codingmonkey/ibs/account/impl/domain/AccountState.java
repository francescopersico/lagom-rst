package cz.codingmonkey.ibs.account.impl.domain;

import com.lightbend.lagom.serialization.Jsonable;
import cz.codinmonkey.ibs.account.api.Account;
import org.apache.commons.lang3.Validate;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.*;
import static java.util.Objects.requireNonNull;

/**
 * @author rstefanca
 */
public class AccountState implements Jsonable {

	private final Optional<Account> account;

	private final BigDecimal balance;

	public AccountState(Optional<Account> account, BigDecimal balance) {
		this.account = account;
		this.balance = balance;
	}

	public static AccountState notCreated() {
		return new AccountState(Optional.empty(), ZERO);
	}

	public static AccountState create(Account account) {
		return new AccountState(Optional.of(account), ZERO);
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public AccountState addMovement(Movement movement) {
		requireNonNull(movement);
		BigDecimal newBalance = balance.add(movement.getAmount());
		if (newBalance.compareTo(ZERO) < 0) {
			throw new RuntimeException("Overdrawn!"); //todo typed exception
		}

		return new AccountState(account, newBalance);
	}
}
