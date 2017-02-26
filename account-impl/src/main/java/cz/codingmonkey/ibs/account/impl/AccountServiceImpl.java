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
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import cz.codingmonkey.ibs.account.impl.domain.AccountCommand;
import cz.codingmonkey.ibs.account.impl.domain.AccountEntity;
import cz.codingmonkey.ibs.user.api.ClientMessage;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkeys.cbs.api.CbsClient;
import cz.codingmonkeys.cbs.api.CbsClientService;
import cz.codinmonkey.ibs.account.api.Account;
import cz.codinmonkey.ibs.account.api.AccountService;
import cz.codinmonkey.ibs.account.api.Deposit;
import cz.codinmonkey.ibs.account.api.Movement;
import lombok.extern.slf4j.Slf4j;
import org.pcollections.PSequence;

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
	public AccountServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ClientService clientService, CbsClientService cbsClientService, Materializer mat, ReadSide readSide) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.cbsClientService = cbsClientService;
		this.mat = mat;
		this.persistentEntityRegistry.register(AccountEntity.class);

		readSide.register(AccountEventProcessor.class);

		//subscribe to client events
		clientService.clientsTopic().subscribe().atLeastOnce(Flow.fromFunction(this::handleMessage));
	}

	@Override
	public ServiceCall<NotUsed, Account> getAccount(String iban) {
		return request -> entityRef(iban).ask(AccountCommand.GetInfo.INSTANCE);
	}

	@Override
	public ServiceCall<NotUsed, PSequence<Movement>> getMovements(String iban) {
		return null;
	}

	@Override
	public ServiceCall<Deposit, Done> deposit(String iban) {
		return request ->
				entityRef(iban).ask(new AccountCommand.Deposit(request.getOtherIban(), request.getAmount()));
	}

	private Done handleMessage(ClientMessage msg) {
		log.info("Received client message");
		if (msg instanceof ClientMessage.ClientCreated) {
			log.info("Handling {}", msg);
			cbsClientService.getCbsClient(msg.getExternalClientId())
					.thenCompose(cbsClient -> createAccount(cbsClient, msg.getExternalClientId()));
		}
		return Done.getInstance();
	}

	private PersistentEntityRef<AccountCommand> entityRef(String entityId) {
		return persistentEntityRegistry.refFor(AccountEntity.class, entityId);
	}

	private CompletionStage<Done> createAccount(CbsClient cbsClient, String externalClientId) {
		return Source.from(cbsClient.getAccounts()).mapAsyncUnordered(4, acc -> {
			log.info("Adding account {}", acc.getIban());
			return entityRef(acc.getIban()).ask(new AccountCommand.AddAccount(acc.getIban(), externalClientId));
		}).runWith(Sink.ignore(), mat).toCompletableFuture();
	}
}
