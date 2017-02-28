package cz.codingmonkey.ibs.user.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */
@Immutable
@JsonDeserialize
@Value
@Builder
public class Client {

	public final String externalId;
	public final String email;
	public final String sms;
	public final boolean vip;
	public final boolean active;

}
