package cz.codingmonkey.ibs.fee.api;

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
public interface FeeService extends Service {

	ServiceCall<NotUsed, FeeInfo> getFeeForClient(String clientId);

	@Override
	default Descriptor descriptor() {

		return named("fees").withCalls(
				pathCall("/api/fees/fee?clientId", this::getFeeForClient)

		)
				.withAutoAcl(true)
				.withCircuitBreaker(CircuitBreaker.perNode());

	}
}
