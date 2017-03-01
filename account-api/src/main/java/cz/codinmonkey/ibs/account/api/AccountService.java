package cz.codinmonkey.ibs.account.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.CircuitBreaker;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import org.pcollections.PSequence;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * @author rstefanca
 */
public interface AccountService extends Service {

	String ACCOUNTS_TOPIC = "accounts";

	/**
	 * curl http://localhost:9000/api/accounts/32424234242424
	 */
	ServiceCall<NotUsed, Account> getAccount(String iban);

	/**
	 * curl http://localhost:9000/api/accounts/32424234242424/movements
	 */
	ServiceCall<NotUsed, PSequence<PaymentInfo>> getMovements(String iban);

	/**
	 * curl -X POST -d '{"otherIban": "2342342423424242","amount": 1000.50}' "http://localhost:9000/api/accounts/iban2-3605796003289369500/deposit"
	 */
	ServiceCall<Payment, Done> deposit(String iban);

	ServiceCall<Payment, Done> withdraw(String iban);

	ServiceCall<Fee, Done> chargeFee(String iban);

	Topic<AccountMessage> accountsTopic();

	@Override
	default Descriptor descriptor() {

		return named("accounts").withCalls(
				pathCall("/api/accounts/:iban", this::getAccount),
				pathCall("/api/accounts/:iban/movements", this::getMovements),
				pathCall("/api/accounts/:iban/deposit", this::deposit),
				pathCall("/api/accounts/:iban/withdraw", this::withdraw),
				pathCall("/api/accounts/:iban/chargeFee", this::chargeFee)
		)
				.withAutoAcl(true)
				.withCircuitBreaker(CircuitBreaker.perNode())
				.publishing(Service.topic(ACCOUNTS_TOPIC, this::accountsTopic));

	}
}
