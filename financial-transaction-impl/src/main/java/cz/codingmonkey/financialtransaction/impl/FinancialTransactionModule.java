package cz.codingmonkey.financialtransaction.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import cz.codingmonkey.financialtransaction.api.FinancialTransactionService;

/**
 * @author rstefanca
 */
public class FinancialTransactionModule extends AbstractModule implements ServiceGuiceSupport {
	@Override
	protected void configure() {

		bindServices(
				serviceBinding(FinancialTransactionService.class, FinancialTransactionServiceImpl.class)
		);
	}
}
