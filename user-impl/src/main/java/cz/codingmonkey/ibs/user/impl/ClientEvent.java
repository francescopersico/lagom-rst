package cz.codingmonkey.ibs.user.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codingmonkey.ibs.user.api.Client;

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

	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	final class ClientCreated implements ClientEvent {
		public final Client client;


		public ClientCreated(Client client) {
			this.client = client;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ClientCreated that = (ClientCreated) o;
			return Objects.equal(client, that.client);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(client);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
					.add("client", client)
					.toString();
		}
	}

	@Immutable
	@JsonDeserialize
	final class ClientDeactivated implements ClientEvent {
		public final String id;

		public ClientDeactivated(String id) {
			this.id = id;
		}
	}
}
