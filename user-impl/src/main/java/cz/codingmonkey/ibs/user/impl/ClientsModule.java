package cz.codingmonkey.ibs.user.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkey.ibs.user.impl.data.ClientsDatabase;
import cz.codingmonkey.ibs.user.impl.data.ClientsDatabaseImpl;
import cz.codingmonkey.ibs.user.impl.data.ReadRepository;
import cz.codingmonkey.ibs.user.impl.data.ReadRepositoryImpl;
import cz.codingmonkeys.cbs.api.CbsClientService;

/**
 * @author rstefanca
 */
public class ClientsModule extends AbstractModule implements ServiceGuiceSupport {
	@Override
	protected void configure() {
		bind(CbsClientService.class).to(DummyCbsGetClientService.class);
		bind(ClientsDatabase.class).to(ClientsDatabaseImpl.class);
		bind(ReadRepository.class).to(ReadRepositoryImpl.class);

		bindServices(
				serviceBinding(ClientService.class, ClientServiceImpl.class)
		);
	}
}
