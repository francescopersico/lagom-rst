package cz.codinmonkey.ibs.account.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author rstefanca
 */
@Value
@Builder
@EqualsAndHashCode
@ToString
public class Movement {

	@NonNull
	private final String otherIban;

	@NonNull
	private final BigDecimal amount;

	@NonNull
	private final Instant time;

	@JsonCreator
	public Movement(String otherIban, BigDecimal amount, Instant time) {
		this.otherIban = otherIban;
		this.amount = amount;
		this.time = time;
	}
}
