package cz.codingmonkey.ibs.fee.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;

/**
 * @author rstefanca
 */
@Value
@Builder
@EqualsAndHashCode
@ToString
public class FeeInfo {

	private final BigDecimal rate;
}
