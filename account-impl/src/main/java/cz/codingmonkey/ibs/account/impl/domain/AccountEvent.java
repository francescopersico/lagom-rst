package cz.codingmonkey.ibs.account.impl.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codinmonkey.ibs.account.api.Movement;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

/**
 * @author Richard Stefanca
 */
public interface AccountEvent extends Jsonable, AggregateEvent<AccountEvent> {

	AggregateEventTag<AccountEvent> ACCOUNT_EVENT_TAG = AggregateEventTag.of(AccountEvent.class);

	@Override
	default AggregateEventTag<AccountEvent> aggregateTag() {
		return ACCOUNT_EVENT_TAG;
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	class AccountAdded implements AccountEvent {
		public final String iban;
		public final String clientId;

		public AccountAdded(String iban, String clientId) {
			this.iban = iban;
			this.clientId = clientId;
		}
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	final class MoneyTransferred implements AccountEvent {

		public final String paymentId;
		public final String clientId;
		public final String iban;
		public final Movement movement;


		public MoneyTransferred(String paymentId, String clientId, String iban, Movement movement) {
			this.paymentId = paymentId;
			this.clientId = clientId;
			this.iban = iban;
			this.movement = movement;
		}
	}
}
