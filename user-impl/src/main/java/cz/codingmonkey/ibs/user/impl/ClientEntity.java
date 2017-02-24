package cz.codingmonkey.ibs.user.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import cz.codingmonkey.ibs.user.api.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static cz.codingmonkey.ibs.user.impl.ClientCommand.AddClient;
import static cz.codingmonkey.ibs.user.impl.ClientCommand.DeactivateClient;
import static cz.codingmonkey.ibs.user.impl.ClientEvent.ClientCreated;
import static cz.codingmonkey.ibs.user.impl.ClientEvent.ClientDeactivated;

/**
 * @author rstefanca
 */
public class ClientEntity extends PersistentEntity<ClientCommand, ClientEvent, ClientState> {

	private final Logger log = LoggerFactory.getLogger(ClientEntity.class);

	@Override
	public Behavior initialBehavior(Optional<ClientState> snapshotState) {
		if (!snapshotState.isPresent()) {
			return notCreated();
		} else
			return activated(snapshotState.get());
	}

	@SuppressWarnings("unchecked")
	private Behavior notCreated() {
		BehaviorBuilder b = newBehaviorBuilder(ClientState.notCreated());

		b.setCommandHandler(AddClient.class, this::createClientHandler);
		b.setEventHandlerChangingBehavior(ClientCreated.class, evt -> activated(ClientState.active(evt.client)));

		b.setReadOnlyCommandHandler(DeactivateClient.class, (cmd, ctx) ->
				ctx.invalidCommand("Client does not exists. entityId: " + entityId())
		);

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Behavior activated(ClientState clientState) {
		BehaviorBuilder b = newBehaviorBuilder(clientState);

		b.setCommandHandler(DeactivateClient.class, (cmd, ctx) ->
				ctx.thenPersist(new ClientDeactivated(entityId()), evt -> ctx.reply(Done.getInstance())));

		b.setEventHandlerChangingBehavior(ClientDeactivated.class, evt -> deactivated(state().deactivate()));

		addReadOnlyCreateClientHandler(b);

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Behavior deactivated(ClientState clientState) {
		BehaviorBuilder b = newBehaviorBuilder(clientState);

		b.setCommandHandler(ClientCommand.ActivateClient.class, (cmd, ctx) ->
				ctx.thenPersist(new ClientDeactivated(entityId()), evt -> ctx.reply(Done.getInstance()))
		);
		b.setEventHandlerChangingBehavior(ClientDeactivated.class, evt -> activated(state().activate()));

		addReadOnlyCreateClientHandler(b);

		return b.build();
	}

	private static void addReadOnlyCreateClientHandler(BehaviorBuilder b) {
		b.setReadOnlyCommandHandler(AddClient.class, (cmd, ctx) ->
				ctx.invalidCommand("Client already exists")
		);
	}



	@SuppressWarnings("unchecked")
	private Persist createClientHandler(AddClient cmd, CommandContext ctx) {
		log.info("Creating user: {}", cmd);
		Client client = new Client(cmd.externalId, cmd.email, cmd.sms, true);

		return ctx.thenPersist(new ClientCreated(entityId(), client), evt -> ctx.reply(entityId()));
	}
}
