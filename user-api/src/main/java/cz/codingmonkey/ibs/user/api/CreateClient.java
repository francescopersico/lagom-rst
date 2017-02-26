package cz.codingmonkey.ibs.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */

@Immutable
@JsonDeserialize
public class CreateClient {

	public final String externalId;

	@JsonCreator
	public CreateClient(String externalId) {
		this.externalId = externalId;
	}
}
