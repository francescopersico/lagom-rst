package cz.codingmonkey.ibs.account.impl.domain;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import cz.codingmonkey.ibs.account.impl.domain.AccountEvent.MoneyTransferred;
import cz.codinmonkey.ibs.account.api.Account;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author rstefanca
 */
@Slf4j
public class AccountEntity extends PersistentEntity<AccountCommand, AccountEvent, AccountState> {
	@Override
	public Behavior initialBehavior(Optional<AccountState> snapshotState) {
		return snapshotState.map(this::created).orElseGet(this::notCreated);
	}

	@SuppressWarnings("unchecked")
	private Behavior notCreated() {
		BehaviorBuilder b = newBehaviorBuilder(AccountState.notCreated());

		b.setCommandHandler(AccountCommand.AddAccount.class, (cmd, ctx) -> {
			log.info("Creating account {}", cmd.iban);
			return ctx.thenPersist(new AccountEvent.AccountAdded(cmd.iban), evt -> ctx.reply(Done.getInstance()));
		});

		b.setEventHandlerChangingBehavior(AccountEvent.AccountAdded.class, evt -> created(AccountState.create(new Account(evt.iban))));

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Behavior created(AccountState accountState) {
		BehaviorBuilder b = newBehaviorBuilder(accountState);

		b.setReadOnlyCommandHandler(AccountCommand.AddAccount.class, (cmd, ctx) -> ctx.invalidCommand("Not Supported"));

		b.setCommandHandler(AccountCommand.MovementCommand.class, this::handleMovement);
		b.setEventHandler(MoneyTransferred.class, evt -> state().addMovement(evt.movement));

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Persist handleMovement(AccountCommand.MovementCommand cmd, CommandContext ctx) {
		BigDecimal realAmount;
		if (cmd instanceof AccountCommand.Withdraw) {
			realAmount = cmd.getAmount().negate();
		} else if (cmd instanceof AccountCommand.Deposit) {
			realAmount = cmd.getAmount();
		} else {
			throw new IllegalStateException("Unknown movement command " + cmd.getClass());
		}

		if (state().getBalance().add(realAmount).compareTo(BigDecimal.ZERO) < 0) {
			throw new OverdrawException("Not enough money :(");
		}

		Movement movement = new Movement(cmd.getOtherIban(), realAmount, DateTime.now());
		return ctx.thenPersist(new MoneyTransferred(entityId(), movement), evt -> ctx.reply(Done.getInstance()));
	}
}
