package cz.codingmonkey.ibs.account.impl.domain;

import cz.codinmonkey.ibs.account.api.Account;

import java.util.Optional;

/**
 * @author Richard Stefanca
 */
public class AccountState {

	private final Optional<Account> account;

	public AccountState(Optional<Account> account) {
		this.account = account;
	}

	public static AccountState notCreated() {
		return new AccountState(Optional.empty());
	}

	public static AccountState created(Account account) {
		return new AccountState(Optional.of(account));
	}
}
