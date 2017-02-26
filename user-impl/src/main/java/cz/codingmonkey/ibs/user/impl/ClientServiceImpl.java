package cz.codingmonkey.ibs.user.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.api.transport.PolicyViolation;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import cz.codingmonkey.ibs.user.api.Client;
import cz.codingmonkey.ibs.user.api.ClientMessage;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkey.ibs.user.api.CreateClient;
import cz.codingmonkey.ibs.user.impl.data.ReadRepository;
import cz.codingmonkey.ibs.user.impl.domain.ClientCommand;
import cz.codingmonkey.ibs.user.impl.domain.ClientEntity;
import cz.codingmonkey.ibs.user.impl.domain.ClientEvent;
import cz.codingmonkeys.cbs.api.CbsClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static cz.codingmonkey.ibs.user.impl.domain.ClientCommand.AddClient;
import static cz.codingmonkey.ibs.user.impl.domain.ClientCommand.DeactivateClient;

/**
 * @author rstefanca
 */
//TODO clean up code
public class ClientServiceImpl implements ClientService {

	private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

	private final PersistentEntityRegistry persistentEntityRegistry;
	private final CbsClientService cbsClientService;
	private final ReadRepository repository;

	@Inject
	public ClientServiceImpl(
			PersistentEntityRegistry persistentEntityRegistry,
			ReadSide readSide,
			CbsClientService cbsClientService,
			ReadRepository repository) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.cbsClientService = cbsClientService;
		this.repository = repository;
		this.persistentEntityRegistry.register(ClientEntity.class);
		readSide.register(ClientEventProcessor.class);
	}

	@Override
	public ServiceCall<cz.codingmonkey.ibs.user.api.CreateClient, String> createClient() {
		return request -> checkClientWithExternalIdDoesNotExists(request)
				.thenCompose(externalId -> {
					log.info("Getting client with external id {} from CBS", request.externalId);
					return cbsClientService.getCbsClient(request.externalId).thenCompose(cbsClient -> {
						log.info("Retrieved client from CBS: {}", cbsClient);
						String entityId = UUID.randomUUID().toString();
						log.info("===> Creating client {}", entityId);
						PersistentEntityRef<ClientCommand> ref = entityRef(entityId);
						return ref.ask(new AddClient(
								cbsClient.getExternalClientId(),
								cbsClient.getEmail(),
								cbsClient.getSms()));
					});
				});
	}

	@Override
	public ServiceCall<NotUsed, Done> deactivateClient(String id) {
		return request -> {
			log.info("Deactivating client: {}", id);
			return entityRef(id).ask(DeactivateClient.INSTANCE);
		};
	}


	@Override
	public ServiceCall<NotUsed, Client> getClient(String id) {
		return request ->
				repository.getClient(id)
						.thenApply(maybeClient -> {
							if (maybeClient.isPresent()) {
								return maybeClient.get();
							} else {
								throw new NotFound("Client not found");
							}
						});
	}

	@Override
	@SuppressWarnings("unchecked")
	public Topic<ClientMessage> clientsTopic() {
		return TopicProducer.singleStreamWithOffset(offset -> {
			return persistentEntityRegistry
					.eventStream(ClientEvent.CLIENT_EVENT_TAG, offset)
					.filter(pair -> pair.first() instanceof ClientEvent.ClientCreated)
					.map(pair -> {
						ClientEvent.ClientCreated event = (ClientEvent.ClientCreated) pair.first();
						ClientMessage message = new ClientMessage.ClientCreated(event.id, event.client.email, event.client.sms);
						log.info("Publishing {}", message);
						return Pair.create(message, pair.second());
					});
		});
	}

	private PersistentEntityRef<ClientCommand> entityRef(String entityId) {
		return persistentEntityRegistry.refFor(ClientEntity.class, entityId);
	}

	private CompletionStage<String> checkClientWithExternalIdDoesNotExists(CreateClient request) {
		return repository.clientWithExternalIdExists(request.externalId)
				.thenApply(found -> {
					if (found) {
						throw new PolicyViolation("Client with given external id exists");
					}
					return request.externalId;
				});
	}
}
