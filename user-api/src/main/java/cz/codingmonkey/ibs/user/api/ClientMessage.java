package cz.codingmonkey.ibs.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

/**
 * @author rstefanca
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
		defaultImpl = Void.class)
@JsonSubTypes({
		@JsonSubTypes.Type(ClientMessage.ClientCreated.class)
})
public interface ClientMessage {

	String getExternalClientId();

	@JsonTypeName("created")
	@Getter
	@ToString
	final class ClientCreated implements ClientMessage {

		private final String externalClientId;
		private final String email;
		private final String sms;

		@JsonCreator
		public ClientCreated(String clientId, String email, String sms) {
			this.externalClientId = clientId;
			this.email = email;
			this.sms = sms;
		}
	}
}
