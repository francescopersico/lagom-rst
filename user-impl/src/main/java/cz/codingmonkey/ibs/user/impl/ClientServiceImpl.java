package cz.codingmonkey.ibs.user.impl;

import akka.Done;
import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import cz.codingmonkey.ibs.user.api.Client;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkeys.cbs.api.CbsClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static cz.codingmonkey.ibs.user.impl.ClientCommand.CreateClient;
import static cz.codingmonkey.ibs.user.impl.ClientCommand.DeactivateClient;

/**
 * @author rstefanca
 */
public class ClientServiceImpl implements ClientService {

	private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

	private final PersistentEntityRegistry persistentEntityRegistry;

	private final CbsClientService cbsClientService;


	@Inject
	public ClientServiceImpl(PersistentEntityRegistry persistentEntityRegistry, CbsClientService cbsClientService) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.cbsClientService = cbsClientService;
		persistentEntityRegistry.register(ClientEntity.class);
	}

	@Override
	public ServiceCall<cz.codingmonkey.ibs.user.api.CreateClient, String> createClient() {
		return request -> {
			log.info("Getting client with external id {} from CBS", request.externalId);
			return cbsClientService.getCbsClient(request.externalId).thenCompose(cbsClient -> {
				log.info("Retrieved client from CBS: {}", cbsClient);
				String entityId = UUID.randomUUID().toString();
				log.info("===> Creating client {}", entityId);
				PersistentEntityRef<ClientCommand> ref = entityRef(entityId);
				return ref.ask(new CreateClient(
						cbsClient.getExternalClientId(),
						cbsClient.getEmail(),
						cbsClient.getSms()));
			});
		};
	}

	@Override
	public ServiceCall<NotUsed, Done> deactivateClient(String id) {
		return request -> {
			log.info("Deactivating client: {}", id);
			return entityRef(id).ask(DeactivateClient.INSTANCE);
		};
	}

	@Override
	public ServiceCall<String, Client> getClient(String id) {
		//TODO implement read side
		//return request -> CompletableFuture.completedFuture(new Client());
		throw new UnsupportedOperationException("Not implemented");
	}

	private PersistentEntityRef<ClientCommand> entityRef(String entityId) {

		return persistentEntityRegistry.refFor(ClientEntity.class, entityId);
	}


}
