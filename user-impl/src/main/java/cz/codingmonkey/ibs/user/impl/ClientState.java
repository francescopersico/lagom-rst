package cz.codingmonkey.ibs.user.impl;

import com.lightbend.lagom.serialization.CompressedJsonable;
import cz.codingmonkey.ibs.user.api.Client;

import java.util.Optional;

/**
 * @author rstefanca
 */
public class ClientState implements CompressedJsonable {

	private static final long serialVersionUID = 1L;

	private final Optional<Client> client;

	private final boolean active;

	private ClientState(Optional<Client> client, boolean active) {
		this.client = client;
		this.active = active;
	}

	public Optional<Client> getClient() {
		return client;
	}

	static ClientState notCreated() {
		return new ClientState(Optional.empty(), false);
	}

	static ClientState active(Client client) {
		return new ClientState(Optional.of(client), true);
	}

	ClientState deactivate() {
		return new ClientState(client, false);
	}

	ClientState activate() {
		return new ClientState(client, true);
	}
}
