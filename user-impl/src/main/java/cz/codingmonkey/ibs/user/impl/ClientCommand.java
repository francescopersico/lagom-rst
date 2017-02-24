package cz.codingmonkey.ibs.user.impl;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */
public interface ClientCommand extends Jsonable {


	@SuppressWarnings("serial")
	@Immutable
	@JsonDeserialize
	final class CreateClient implements ClientCommand, CompressedJsonable, PersistentEntity.ReplyType<String> {


		public final String externalId;
		public final String email;
		public final String sms;

		public CreateClient(String externalId, String email, String sms) {
			this.externalId = externalId;
			this.email = email;
			this.sms = sms;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			CreateClient that = (CreateClient) o;
			return Objects.equal(externalId, that.externalId) &&
					Objects.equal(email, that.email) &&
					Objects.equal(sms, that.sms);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(externalId, email, sms);
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)

					.add("externalId", externalId)
					.add("email", email)
					.add("sms", sms)
					.toString();
		}
	}


	enum DeactivateClient implements ClientCommand, PersistentEntity.ReplyType<Done> {
		INSTANCE
	}

	enum ActivateClient implements ClientCommand, PersistentEntity.ReplyType<Done> {
		INSTANCE
	}



}
