package cz.codinmonkey.ibs.account.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;

/**
 * @author Richard Stefanca
 */
@Value
@Builder
@EqualsAndHashCode
@ToString
public class Fee {

	private final String iban; //fee actor iban
	private final BigDecimal amount;

}
