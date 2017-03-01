package cz.codingmonkey.ibs.account.impl.domain;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import cz.codingmonkey.ibs.account.impl.domain.AccountEvent.MoneyTransferred;
import cz.codinmonkey.ibs.account.api.Account;
import cz.codinmonkey.ibs.account.api.Movement;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static cz.codingmonkey.ibs.account.impl.domain.AccountCommand.*;

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

		b.setCommandHandler(AddAccount.class, (cmd, ctx) -> {
			log.info("Creating account {}", cmd.iban);
			return ctx.thenPersist(new AccountEvent.AccountAdded(cmd.iban, cmd.clientId), evt -> ctx.reply(Done.getInstance()));
		});

		b.setEventHandlerChangingBehavior(AccountEvent.AccountAdded.class, evt -> created(AccountState.create(new Account(evt.clientId, evt.iban, BigDecimal.ZERO))));

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Behavior created(AccountState accountState) {
		BehaviorBuilder b = newBehaviorBuilder(accountState);

		b.setReadOnlyCommandHandler(AddAccount.class, (cmd, ctx) -> ctx.invalidCommand("Not Supported"));

		addMoneyMovementBehavior(b);

		b.setReadOnlyCommandHandler(GetInfo.class, (cmd, ctx) -> ctx.reply(state().getAccount().get()));

		return b.build();
	}

	private void addMoneyMovementBehavior(BehaviorBuilder b) {
		b.setCommandHandler(Deposit.class, this::handleMovement);
		b.setCommandHandler(Withdraw.class, this::handleMovement);
		b.setCommandHandler(ChargeFee.class, this::handleMovement);

		b.setEventHandler(MoneyTransferred.class, evt -> state().addMovement(evt.movement));
	}

	@SuppressWarnings("unchecked")
	private Persist handleMovement(MovementCommand cmd, CommandContext ctx) {
		Account account = state().getAccount().orElseThrow(IllegalStateException::new);
		BigDecimal realAmount;
		boolean feePayment = false;
		if (cmd instanceof Withdraw) {
			realAmount = cmd.getAmount().negate();
		} else if (cmd instanceof Deposit) {
			realAmount = cmd.getAmount();
		} else if (cmd instanceof ChargeFee) {
			realAmount = cmd.getAmount().negate();
			feePayment = true;
		} else {
			throw new IllegalStateException("Unknown movement command " + cmd.getClass());
		}

		if (!feePayment) {
			BigDecimal newAmount = account.getBalance().add(realAmount);
			if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
				throw new OverdrawException("Not enough money :(");
			}
		}

		Movement movement = new Movement(cmd.getOtherIban(), realAmount, Instant.now());
		log.info("Transfer on {}: {}", entityId(), movement);
		return ctx.thenPersist(new MoneyTransferred(UUID.randomUUID().toString(), account.getClientId(), entityId(), movement), evt -> ctx.reply(Done.getInstance()));
	}
}
