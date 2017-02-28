package cz.codingmonkey.ibs.user.impl.domain;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codingmonkey.ibs.user.api.Client;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */
public interface ClientCommand extends Jsonable {


	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	@ToString
	@EqualsAndHashCode
	final class AddClient implements ClientCommand, CompressedJsonable, PersistentEntity.ReplyType<String> {

		public final String externalId;
		public final String email;
		public final String sms;
		public final boolean vip;

		@JsonCreator
		public AddClient(String externalId, String email, String sms, boolean vip) {
			this.externalId = externalId;
			this.email = email;
			this.sms = sms;
			this.vip = vip;
		}
	}


	enum DeactivateClient implements ClientCommand, PersistentEntity.ReplyType<Done> {
		INSTANCE
	}

	enum ActivateClient implements ClientCommand, PersistentEntity.ReplyType<Done> {
		INSTANCE
	}

	enum GetInfo implements ClientCommand, PersistentEntity.ReplyType<Client> {
		INSTANCE
	}

}
