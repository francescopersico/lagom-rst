package cz.codingmonkey.ibs.account.impl.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
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

		public AccountAdded(String iban) {
			this.iban = iban;
		}
	}
}
