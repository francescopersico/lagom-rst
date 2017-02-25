package cz.codingmonkey.ibs.user.impl.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codingmonkey.ibs.user.api.Client;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */
public interface ClientEvent extends Jsonable, AggregateEvent<ClientEvent> {

	AggregateEventTag<ClientEvent> CLIENT_EVENT_TAG = AggregateEventTag.of(ClientEvent.class);

	@Override
	default AggregateEventTag<ClientEvent> aggregateTag() {
		return CLIENT_EVENT_TAG;
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	final class ClientCreated implements ClientEvent {

		public final String id;
		public final Client client;

		public ClientCreated(String id, Client client) {
			this.id = id;
			this.client = client;
		}
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	final class ClientDeactivated implements ClientEvent {
		public final String id;

		public ClientDeactivated(String id) {
			this.id = id;
		}
	}
}
