package cz.codingmonkey.ibs.fee.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import cz.codingmonkey.ibs.fee.api.FeeInfo;
import cz.codingmonkey.ibs.fee.api.FeeService;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codinmonkey.ibs.account.api.AccountMessage;
import cz.codinmonkey.ibs.account.api.AccountService;
import cz.codinmonkey.ibs.account.api.Fee;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.CompletionStage;

/**
 * @author rstefanca
 */

@Slf4j
public class FeeServiceImpl implements FeeService {

	private final PersistentEntityRegistry persistentEntityRegistry;
	private final ClientService clientService;
	private final AccountService accountService;
	private String myIban = "feeIban-23232323232323";

	@Inject
	public FeeServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ClientService clientService, AccountService accountService) {
		this.persistentEntityRegistry = persistentEntityRegistry;
		this.clientService = clientService;
		this.accountService = accountService;

		//subscribe to client events
		accountService.accountsTopic().subscribe().atLeastOnce(Flow.fromFunction(this::handleMessage));
	}

	private Done handleMessage(AccountMessage msg) {
		log.info("Handling massage {}", msg);

		if (msg instanceof AccountMessage.Movement) {
			AccountMessage.Movement movement = (AccountMessage.Movement) msg;
			String toIban = movement.getOtherIban();

			if (!myIban.equals(toIban) && movement.getAmount().compareTo(BigDecimal.ZERO) < 0) { // don't charge yourself & withdraw only
				BigDecimal amount = movement.getAmount().abs();
				getRate(movement.getClientId())
						.thenApply(rate -> Fee.builder()
								.iban(myIban)
								.amount(rate.multiply(amount, new MathContext(2, RoundingMode.CEILING)))
								.build())
						.thenCompose(fee -> accountService.chargeFee(movement.getAccountIban()).invoke(fee))
						.thenRun(() -> log.info("Fee for {} charged", movement));
			}
		}

		return Done.getInstance();
	}

	@Override
	public ServiceCall<NotUsed, FeeInfo> getFeeForClient(String clientId) {
		return request -> clientService
				.getClient(clientId)
				.invoke()
				.thenCompose(client -> getRate(client.externalId))
				.thenApply(rate -> FeeInfo.builder().rate(rate).build());
	}

	//simple rate calculation
	private CompletionStage<BigDecimal> getRate(String clientId) {
		return clientService.getClient(clientId)
				.invoke()
				.thenApply(client ->
						client.vip ? BigDecimal.valueOf(.001) : BigDecimal.valueOf(.005)
				);
	}
}
