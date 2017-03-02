package cz.codingmonkey.financialtransaction.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import cz.codingmonkey.financialtransaction.api.FinancialTransactionService;

import java.util.concurrent.CompletableFuture;

/**
 * @author rstefanca
 */
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

	@Override
	public ServiceCall<NotUsed, String> ping() {
		return request -> CompletableFuture.completedFuture("pong");
	}

}
