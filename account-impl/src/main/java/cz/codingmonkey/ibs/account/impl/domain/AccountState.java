package cz.codingmonkey.ibs.account.impl.domain;

import com.lightbend.lagom.serialization.Jsonable;
import cz.codinmonkey.ibs.account.api.Account;
import cz.codinmonkey.ibs.account.api.Movement;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;
import static java.util.Objects.requireNonNull;

/**
 * @author rstefanca
 */
public class AccountState implements Jsonable {

	private final Optional<Account> account;

	public AccountState(Optional<Account> account) {
		this.account = account;
	}

	public static AccountState notCreated() {
		return new AccountState(Optional.empty());
	}

	public static AccountState create(Account account) {
		return new AccountState(Optional.of(account));
	}


	public AccountState addMovement(Movement movement) {
		requireNonNull(movement);
		Account account = this.account.orElseThrow(IllegalStateException::new);

		BigDecimal newBalance = account.getBalance().add(movement.getAmount());
		if (newBalance.compareTo(ZERO) < 0) {
			throw new RuntimeException("Overdrawn!"); //todo typed exception
		}

		return new AccountState(Optional.of(account.withBalance(newBalance)));
	}

	public Optional<Account> getAccount() {
		return account;
	}
}
