package cz.codinmonkey.ibs.account.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author rstefanca
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
		defaultImpl = Void.class)
@JsonSubTypes({
		@JsonSubTypes.Type(AccountMessage.Movement.class)
})
public interface AccountMessage {

	String getAccountIban();

	@JsonTypeName("movement")
	@Getter
	@ToString
	@AllArgsConstructor
	final class Movement implements AccountMessage {
		private final String accountIban;
		private final String otherIban;
		private final String clientId;
		private final String movementId;
		private final BigDecimal amount;
	}
}
