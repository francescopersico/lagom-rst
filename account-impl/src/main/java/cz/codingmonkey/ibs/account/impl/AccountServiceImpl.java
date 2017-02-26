package cz.codingmonkey.ibs.account.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import cz.codingmonkey.ibs.account.impl.domain.AccountCommand;
import cz.codingmonkey.ibs.account.impl.domain.AccountEntity;
import cz.codingmonkey.ibs.user.api.ClientMessage;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkeys.cbs.api.CbsClient;
import cz.codingmonkeys.cbs.api.CbsClientService;
import cz.codinmonkey.ibs.account.api.Account;
import cz.codinmonkey.ibs.account.api.AccountService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author rstefanca
 */
@Slf4j
public class AccountServiceImpl implements AccountService {

	private final PersistentEntityRegistry persistentEntityRegistry;
	private final CbsClientService cbsClientService;
	private final Materializer mat;

	@Inject
	public AccountServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ClientService clientService, CbsClientService cbsClientService, Materializer mat) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.cbsClientService = cbsClientService;
		this.mat = mat;
		this.persistentEntityRegistry.register(AccountEntity.class);

		//subscribe to client events
		clientService.clientsTopic().subscribe().atLeastOnce(Flow.fromFunction(this::handleMessage));
	}

	@Override
	public ServiceCall<NotUsed, Account> getAccount(String iban) {
		return request -> CompletableFuture.completedFuture(new Account("IBN3242432424"));
	}

	private Done handleMessage(ClientMessage msg) {
		log.info("Received client message");
		if (msg instanceof ClientMessage.ClientCreated) {
			log.info("Handling {}", msg);
			cbsClientService.getCbsClient(msg.getExternalClientId()).thenCompose(this::createAccount);
		}
		return Done.getInstance();
	}

	private PersistentEntityRef<AccountCommand> entityRef(String entityId) {
		return persistentEntityRegistry.refFor(AccountEntity.class, entityId);
	}

	private CompletionStage<Done> createAccount(CbsClient cbsClient) {
		return Source.from(cbsClient.getAccounts()).mapAsyncUnordered(4, acc -> {
			log.info("Adding account {}", acc.getIban());
			return entityRef(acc.getIban()).ask(new AccountCommand.AddAccount(acc.getIban()));
		}).runWith(Sink.ignore(), mat).toCompletableFuture();
	}
}
