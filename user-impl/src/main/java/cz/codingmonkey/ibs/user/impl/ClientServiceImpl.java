package cz.codingmonkey.ibs.user.impl;

import akka.Done;
import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import cz.codingmonkey.ibs.user.api.Client;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkey.ibs.user.api.CreateClient;
import cz.codingmonkeys.cbs.api.CbsClientService;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static cz.codingmonkey.ibs.user.impl.ClientCommand.AddClient;
import static cz.codingmonkey.ibs.user.impl.ClientCommand.DeactivateClient;

/**
 * @author rstefanca
 */
//TODO clean up code
public class ClientServiceImpl implements ClientService {

	private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

	private final PersistentEntityRegistry persistentEntityRegistry;
	private final CbsClientService cbsClientService;
	private final JdbcSession jdbcSession;

	@Inject
	public ClientServiceImpl(
			PersistentEntityRegistry persistentEntityRegistry,
			ReadSide readSide,
			CbsClientService cbsClientService,
			JdbcSession jdbcSession) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.cbsClientService = cbsClientService;
		this.jdbcSession = jdbcSession;
		this.persistentEntityRegistry.register(ClientEntity.class);
		readSide.register(ClientEventProcessor.class);
	}

	@Override
	public ServiceCall<cz.codingmonkey.ibs.user.api.CreateClient, String> createClient() {
		return request -> {
			return checkClientWithExternalIdDoesNotExists(request)
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

	private CompletionStage<String> checkClientWithExternalIdDoesNotExists(CreateClient request) {
		return jdbcSession.withConnection(connection -> {
					Integer count = new QueryRunner().query(connection, "SELECT COUNT(*) FROM clients WHERE externalId=?", request
							.externalId, resultSet -> {
						resultSet.next();
						return resultSet.getInt(1);
					});
					if (count > 0) throw new RuntimeException("Exists"); //TODO concrete exception
					log.info("No client with external id {} found", request.externalId);
					return request.externalId;
				}
		);
	}
}
