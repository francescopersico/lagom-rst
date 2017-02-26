package cz.codingmonkey.ibs.account.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codingmonkeys.cbs.api.CbsClientService;
import cz.codinmonkey.ibs.account.api.AccountService;

/**
 * @author rstefanca
 */
public class AccountsModule extends AbstractModule implements ServiceGuiceSupport {
	@Override
	protected void configure() {
		bind(CbsClientService.class).to(DummyCbsGetClientService.class);

		bindClient(ClientService.class);

		bindServices(
				serviceBinding(AccountService.class, AccountServiceImpl.class)
		);
	}
}
