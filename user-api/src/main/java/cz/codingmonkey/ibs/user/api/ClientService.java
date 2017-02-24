package cz.codingmonkey.ibs.user.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.CircuitBreaker;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.*;
import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * @author rstefanca
 */
public interface ClientService extends Service {

	ServiceCall<CreateClient, String> createClient();

	ServiceCall<String, Client> getClient(String id);

	ServiceCall<NotUsed, Done> deactivateClient(String id);

	@Override
	default Descriptor descriptor() {

		return named("clients").withCalls(
				pathCall("/api/clients", this::createClient),
				pathCall("/api/clients/:id", this::getClient),
				restCall(Method.PUT, "/api/clients/:id/deactivate", this::deactivateClient)
		).withAutoAcl(true).withCircuitBreaker(CircuitBreaker.perNode());

	}

}
