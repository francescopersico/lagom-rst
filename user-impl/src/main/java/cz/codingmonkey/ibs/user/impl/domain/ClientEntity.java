package cz.codingmonkey.ibs.user.impl.domain;

import akka.Done;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import cz.codingmonkey.ibs.user.api.Client;
import cz.codingmonkey.ibs.user.impl.domain.ClientEvent.ClientActivated;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static cz.codingmonkey.ibs.user.impl.domain.ClientCommand.*;
import static cz.codingmonkey.ibs.user.impl.domain.ClientEvent.ClientCreated;
import static cz.codingmonkey.ibs.user.impl.domain.ClientEvent.ClientDeactivated;

/**
 * @author rstefanca
 */
@Slf4j
@SuppressWarnings("unchecked")
public class ClientEntity extends PersistentEntity<ClientCommand, ClientEvent, ClientState> {

	@Override
	public Behavior initialBehavior(Optional<ClientState> snapshotState) {
		return snapshotState.map(this::activated).orElseGet(this::notCreated);
	}

	@SuppressWarnings("unchecked")
	private Behavior notCreated() {
		BehaviorBuilder b = newBehaviorBuilder(ClientState.notCreated());

		b.setCommandHandler(AddClient.class, this::createClientHandler);
		b.setEventHandlerChangingBehavior(ClientCreated.class, evt -> activated(ClientState.active(evt.client)));

		b.setReadOnlyCommandHandler(DeactivateClient.class, (cmd, ctx) -> {
					throw new NotFound("Client with id " + entityId() + " not found");
				}
		);

		b.setReadOnlyCommandHandler(GetInfo.class, (cmd, ctx) -> {
					throw new NotFound("Client with id " + entityId() + " not found");
				}
		);

		return b.build();
	}

	@SuppressWarnings("unchecked")
	private Behavior activated(ClientState clientState) {
		BehaviorBuilder b = newBehaviorBuilder(clientState);

		setDeactivateBehavior(b);

		addReadOnlyCreateClientHandler(b);

		return b.build();
	}

	private Behavior deactivated(ClientState clientState) {
		BehaviorBuilder b = newBehaviorBuilder(clientState);

		setActivateBehavior(b);

		addReadOnlyCreateClientHandler(b);

		return b.build();
	}

	private void setActivateBehavior(BehaviorBuilder b) {
		b.setCommandHandler(ActivateClient.class, (cmd, ctx) ->
				ctx.thenPersist(new ClientDeactivated(entityId()), evt -> ctx.reply(Done.getInstance()))
		);
		b.setEventHandlerChangingBehavior(ClientActivated.class, evt -> activated(state().activate()));
	}

	private void setDeactivateBehavior(BehaviorBuilder b) {
		b.setCommandHandler(DeactivateClient.class, (cmd, ctx) ->
				ctx.thenPersist(new ClientDeactivated(entityId()), evt -> ctx.reply(Done.getInstance())));

		b.setEventHandlerChangingBehavior(ClientDeactivated.class, evt -> deactivated(state().deactivate()));
	}

	private void addReadOnlyCreateClientHandler(BehaviorBuilder b) {
		b.setReadOnlyCommandHandler(AddClient.class, (cmd, ctx) ->
				ctx.invalidCommand("Client already exists")
		);

		b.setReadOnlyCommandHandler(GetInfo.class, (cmd, ctx) -> ctx.reply(state().getClient().get()));
	}

	private Persist createClientHandler(AddClient cmd, CommandContext ctx) {
		log.info("Creating user: {}", cmd);
		Client client = Client.builder()
				.externalId(cmd.externalId)
				.email(cmd.email)
				.sms(cmd.sms)
				.vip(cmd.vip)
				.active(true)
				.build();

		return ctx.thenPersist(new ClientCreated(entityId(), client), evt -> ctx.reply(entityId()));
	}
}
