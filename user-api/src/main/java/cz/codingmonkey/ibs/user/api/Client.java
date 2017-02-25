package cz.codingmonkey.ibs.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */
@Immutable
@JsonDeserialize
@ToString
@EqualsAndHashCode
@Builder
public class Client {

	public final String externalId;
	public final String email;
	public final String sms;
	public boolean active;

	@JsonCreator
	public Client(String externalId, String email, String sms, boolean active) {
		this.externalId = externalId;
		this.email = email;
		this.sms = sms;
		this.active = active;
	}

}
