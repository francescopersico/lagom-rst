package cz.codingmonkey.financialtransaction.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * @author rstefanca
 */

@Value
@Wither
@Builder
public class Transaction {

	private final String clientId;

	@JsonCreator
	public Transaction(String clientId) {
		this.clientId = clientId;
	}

}
