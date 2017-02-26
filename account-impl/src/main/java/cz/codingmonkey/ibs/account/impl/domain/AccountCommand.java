package cz.codingmonkey.ibs.account.impl.domain;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

/**
 * @author Richard Stefanca
 */
public interface AccountCommand extends Jsonable {

	/**
	 * Marks movements commands
	 */
	interface MovementCommand extends AccountCommand, PersistentEntity.ReplyType<Done>{
		String getOtherIban();
		BigDecimal getAmount();
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	class AddAccount implements AccountCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {

		public final String iban;

		public AddAccount(String iban) {
			this.iban = requireNonNull(iban, "iban must not be null");
		}
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	@Getter
	class Withdraw implements MovementCommand, CompressedJsonable {

		private final String otherIban;
		private final BigDecimal amount;

		public Withdraw(String otherIban, BigDecimal amount) {
			this.otherIban = requireNonNull(otherIban, "otherIban must not be null");
			this.amount = requireNonNull(amount, "amount must not be null");
		}
	}

	@Immutable
	@JsonDeserialize
	@EqualsAndHashCode
	@ToString
	@Getter
	class Deposit implements MovementCommand, CompressedJsonable {

		private final String otherIban;
		private final BigDecimal amount;

		public Deposit(String otherIban, BigDecimal amount) {
			this.otherIban = requireNonNull(otherIban, "otherIban must not be null");
			this.amount = requireNonNull(amount, "amount must not be null");
		}

	}
}
