package cz.codingmonkey.ibs.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		CreateClient that = (CreateClient) o;

		return new EqualsBuilder()
				.append(externalId, that.externalId)

				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(externalId)

				.toHashCode();
	}

	@Override
	public String toString() {
		return "CreateClient{" +
				"externalId='" + externalId + '\'' +
				'}';
	}
}
