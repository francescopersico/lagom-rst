package cz.codingmonkey.ibs.user.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkeys.cbs.api.CbsClientService;

/**
 * @author rstefanca
 */
public class ClientsModule extends AbstractModule implements ServiceGuiceSupport {
	@Override
	protected void configure() {
		bind(CbsClientService.class).to(DummyCbsGetClientService.class);

		bindServices(
				serviceBinding(ClientService.class, ClientServiceImpl.class)
		);
	}
}
