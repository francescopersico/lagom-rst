package cz.codingmonkey.financialtransaction.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.CircuitBreaker;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * @author rstefanca
 */
public interface FinancialTransactionService extends Service {

	ServiceCall<NotUsed, String> ping();

	@Override
	default Descriptor descriptor() {

		return named("transactions")
				.withCalls(
						pathCall("/api/transactions/ping", this::ping))
				.withAutoAcl(true)
				.withCircuitBreaker(CircuitBreaker.perNode());

	}
}
