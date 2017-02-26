package cz.codingmonkey.ibs.account.impl.domain;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

/**
 * @author Richard Stefanca
 */
public interface AccountCommand extends Jsonable {

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	class AddAccount implements AccountCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {

		public final String iban;

		public AddAccount(String iban) {
			this.iban = iban;
		}
	}
}
