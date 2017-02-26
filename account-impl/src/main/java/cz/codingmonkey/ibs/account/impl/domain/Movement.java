package cz.codingmonkey.ibs.account.impl.domain;

import lombok.*;
import org.joda.time.DateTime;

import java.math.BigDecimal;

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
	private final DateTime time;

}
