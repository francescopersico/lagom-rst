package cz.codinmonkey.ibs.account.api;

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
public interface AccountService extends Service {

	ServiceCall<NotUsed, Account> getAccount(String iban);

	@Override
	default Descriptor descriptor() {

		return named("accounts").withCalls(
				pathCall("/api/accounts/:iban", this::getAccount)
		)
				.withAutoAcl(true)
				.withCircuitBreaker(CircuitBreaker.perNode());

	}
}
