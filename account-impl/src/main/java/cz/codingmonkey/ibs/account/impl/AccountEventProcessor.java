package cz.codingmonkey.ibs.account.impl;

import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import cz.codingmonkey.ibs.account.impl.data.AccountsDatabase;
import cz.codingmonkey.ibs.account.impl.domain.AccountEvent;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import static cz.codingmonkey.ibs.account.impl.domain.AccountEvent.ACCOUNT_EVENT_TAG;

/**
 * @author Richard Stefanca
 */
public class AccountEventProcessor extends ReadSideProcessor<AccountEvent> {

	private final JdbcReadSide jdbcReadSide;
	private final AccountsDatabase accountsDatabase;

	@Inject
	public AccountEventProcessor(JdbcReadSide jdbcReadSide, AccountsDatabase accountsDatabase) {
		this.jdbcReadSide = jdbcReadSide;
		this.accountsDatabase = accountsDatabase;
	}

	@Override
	public ReadSideHandler<AccountEvent> buildHandler() {
		JdbcReadSide.ReadSideHandlerBuilder<AccountEvent> builder = jdbcReadSide
				.builder("accountsoffset");

		builder.setGlobalPrepare(accountsDatabase::createTables);
		builder.setEventHandler(AccountEvent.AccountAdded.class, accountsDatabase::createAccount);
		builder.setEventHandler(AccountEvent.MoneyTransferred.class, accountsDatabase::addMovement);

		return builder.build();
	}

	@Override
	public PSequence<AggregateEventTag<AccountEvent>> aggregateTags() {
		return TreePVector.singleton(ACCOUNT_EVENT_TAG);
	}
}
