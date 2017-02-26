package cz.codingmonkey.ibs.account.impl.domain;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import cz.codinmonkey.ibs.account.api.Account;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author rstefanca
 */
@Slf4j
public class AccountEntity extends PersistentEntity<AccountCommand, AccountEvent, AccountState> {
	@Override
	public Behavior initialBehavior(Optional<AccountState> snapshotState) {
		return snapshotState.map(this::activated).orElseGet(this::notCreated);
	}

	@SuppressWarnings("unchecked")
	private Behavior notCreated() {
		BehaviorBuilder b = newBehaviorBuilder(AccountState.notCreated());

		b.setCommandHandler(AccountCommand.AddAccount.class, (cmd, ctx) -> {
			log.info("Creating account {}", cmd.iban);
			return ctx.thenPersist(new AccountEvent.AccountAdded(cmd.iban), evt -> ctx.reply(Done.getInstance()));
		});
		b.setEventHandler(AccountEvent.AccountAdded.class, evt -> AccountState.created(new Account(evt.iban, 0)));

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Behavior activated(AccountState accountState) {
		BehaviorBuilder b = newBehaviorBuilder(accountState);

		b.setReadOnlyCommandHandler(AccountCommand.AddAccount.class, (cmd, ctx) -> ctx.invalidCommand("Not Supported"));

		return b.build();
	}
}
