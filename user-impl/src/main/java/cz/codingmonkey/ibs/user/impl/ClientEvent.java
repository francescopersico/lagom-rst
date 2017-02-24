package cz.codingmonkey.ibs.user.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codingmonkey.ibs.user.api.Client;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

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

		public final String id;
		public final Client client;


		public ClientCreated(String id, Client client) {
			this.id = id;
			this.client = client;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ClientCreated that = (ClientCreated) o;
			return Objects.equals(id, that.id) &&
					Objects.equals(client, that.client);
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, client);
		}

		@Override
		public String toString() {
			return "ClientCreated{" +
					"id='" + id + '\'' +
					", client=" + client +
					'}';
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
