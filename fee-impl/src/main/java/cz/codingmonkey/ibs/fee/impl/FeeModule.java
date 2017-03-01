package cz.codingmonkey.ibs.fee.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import cz.codingmonkey.ibs.fee.api.FeeService;
import cz.codingmonkey.ibs.user.api.ClientService;
import cz.codinmonkey.ibs.account.api.AccountService;

/**
 * @author rstefanca
 */
public class FeeModule extends AbstractModule implements ServiceGuiceSupport {
	@Override
	protected void configure() {

		bindClient(ClientService.class);
		bindClient(AccountService.class);

		bindServices(
				serviceBinding(FeeService.class, FeeServiceImpl.class)
		);
	}
}
