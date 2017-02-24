package cz.codingmonkey.ibs.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.concurrent.Immutable;

/**
 * @author rstefanca
 */
@Immutable
@JsonDeserialize
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Client that = (Client) o;
		return active == that.active &&
				Objects.equal(externalId, that.externalId) &&
				Objects.equal(email, that.email);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(externalId, email, active);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("externalId", externalId)
				.add("email", email)
				.add("active", active)
				.toString();
	}
}
