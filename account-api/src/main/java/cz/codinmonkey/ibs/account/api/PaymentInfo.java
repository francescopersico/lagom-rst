package cz.codinmonkey.ibs.account.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author rstefanca
 */

@Immutable
@JsonDeserialize
@Value
@Builder
public class PaymentInfo {

	private final String id;
	private final String iban;
	private final String otherIban;
	private final BigDecimal amount;
	private final Instant time;



}
