package cz.codingmonkey.financialtransaction.impl.domain;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codingmonkey.financialtransaction.api.Transaction;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author rstefanca
 */
public interface TransactionCommand extends Jsonable {

	@EqualsAndHashCode
	@ToString
	class CreateTransaction implements TransactionCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {

		public final Transaction transaction;

		@JsonCreator
		public CreateTransaction(Transaction transaction) {
			this.transaction = transaction;
		}
	}

}
