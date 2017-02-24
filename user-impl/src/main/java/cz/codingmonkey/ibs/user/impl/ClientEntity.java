package cz.codingmonkey.ibs.user.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import cz.codingmonkey.ibs.user.api.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Function;

import static cz.codingmonkey.ibs.user.impl.ClientCommand.*;
import static cz.codingmonkey.ibs.user.impl.ClientCommand.CreateClient;
import static cz.codingmonkey.ibs.user.impl.ClientEvent.*;

/**
 * @author rstefanca
 */
public class ClientEntity extends PersistentEntity<ClientCommand, ClientEvent, Optional<Client>> {

	private final Logger log = LoggerFactory.getLogger(ClientEntity.class);

	@Override
	public Behavior initialBehavior(Optional<Optional<Client>> snapshotState) {
		Optional<Client> client = snapshotState.flatMap(Function.identity());

		if (client.isPresent()) {
			return activated(client.get());
		} else {
			return notCreated();
		}
	}

	@SuppressWarnings("unchecked")
	private Behavior activated(Client client) {
		BehaviorBuilder b = newBehaviorBuilder(Optional.of(client));

		b.setCommandHandler(DeactivateClient.class, (cmd, ctx) -> {
			log.info("Deactivating client: {}", client);
			if (!client.active) {
				throw new ClientStateException("Client already deactivated");
			}

			return ctx.thenPersist(new ClientDeactivated(entityId()), evt -> ctx.reply(Done.getInstance()));
		});
		b.setEventHandlerChangingBehavior(ClientDeactivated.class, evt -> deactivated(state().get()));

		addReadOnlyCreateClientHandler(b);

		return b.build();
	}

	private static void addReadOnlyCreateClientHandler(BehaviorBuilder b) {
		b.setReadOnlyCommandHandler(CreateClient.class, (cmd, ctx) ->
				ctx.invalidCommand("Client already exists")
		);
	}

	@SuppressWarnings("unchecked")
	private Behavior deactivated(Client client) {
		BehaviorBuilder b = newBehaviorBuilder(Optional.of(client));
		addReadOnlyCreateClientHandler(b);
		return b.build();
	}

	private Client deactivate(Optional<Client> state) {
		Client client = state.get();
		return new Client(client.externalId, client.email, client.sms, false);
	}


	@SuppressWarnings("unchecked")
	private Behavior notCreated() {
		BehaviorBuilder b = newBehaviorBuilder(Optional.empty());

		b.setCommandHandler(CreateClient.class, this::createClientHandler);
		b.setEventHandler(ClientCreated.class, evt -> Optional.of(evt.client));

		b.setReadOnlyCommandHandler(DeactivateClient.class, (cmd, ctx) ->
				ctx.invalidCommand("Client does not exists. entityId: " + entityId())
		);

		b.setEventHandlerChangingBehavior(ClientCreated.class, evt -> activated(evt.client));

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Persist createClientHandler(CreateClient cmd, CommandContext ctx) {
		log.info("Creating user: {}", cmd);
		Client client = new Client(cmd.externalId, cmd.email, cmd.sms, true);

		return ctx.thenPersist(new ClientCreated(client), evt -> ctx.reply(entityId()));
	}
}
