package cz.codinmonkey.ibs.account.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

/**
 * @author Richard Stefanca
 */

@Immutable
@JsonDeserialize
@ToString
@EqualsAndHashCode
@Builder
public class Account {

	private final String iban;

	@JsonCreator
	public Account(String iban) {
		this.iban = iban;
	}
}
