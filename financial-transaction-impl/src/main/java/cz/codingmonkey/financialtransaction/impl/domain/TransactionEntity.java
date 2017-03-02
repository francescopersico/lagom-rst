package cz.codingmonkey.financialtransaction.impl.domain;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

import static cz.codingmonkey.financialtransaction.impl.domain.TransactionEvent.TransactionCreated;

/**
 * @author rstefanca
 */
public class TransactionEntity extends PersistentEntity<TransactionCommand, TransactionEvent, TransactionState> {

	@Override
	@SuppressWarnings("unchecked")
	public Behavior initialBehavior(Optional<TransactionState> snapshotState) {
		if (snapshotState.isPresent() && !snapshotState.get().isEmpty()) {
			// behavior after snapshot must be restored by initialBehavior
			return becomeTransactionCreated(snapshotState.get());
		}

		BehaviorBuilder b = newBehaviorBuilder(TransactionState.EMPTY);

		b.setCommandHandler(TransactionCommand.CreateTransaction.class, (cmd, ctx) -> {
			return ctx.thenPersist(new TransactionCreated(entityId(), cmd.transaction),
					evt -> ctx.reply(Done.getInstance()));
		});

		b.setEventHandlerChangingBehavior(TransactionCreated.class, evt ->
				becomeTransactionCreated(TransactionState.created(evt.transaction)));


		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Behavior becomeTransactionCreated(TransactionState newState) {
		BehaviorBuilder b = newBehaviorBuilder(newState);

		return b.build();
	}


}
