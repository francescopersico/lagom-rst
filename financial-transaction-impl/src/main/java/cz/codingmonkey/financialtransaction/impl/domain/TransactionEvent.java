package cz.codingmonkey.financialtransaction.impl.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codingmonkey.financialtransaction.api.Transaction;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */
public interface TransactionEvent extends Jsonable, AggregateEvent<TransactionEvent> {

	AggregateEventTag<TransactionEvent> TRANSACTION_EVENT_TAG = AggregateEventTag.of(TransactionEvent.class);

	@Override
	default AggregateEventTag<TransactionEvent> aggregateTag() {
		return TRANSACTION_EVENT_TAG;
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	class TransactionCreated implements TransactionEvent {

		public final String transactionId;
		public final Transaction transaction;

		@JsonCreator
		public TransactionCreated(String transactionId, Transaction transaction) {
			this.transactionId = transactionId;
			this.transaction = transaction;
		}
	}
}
