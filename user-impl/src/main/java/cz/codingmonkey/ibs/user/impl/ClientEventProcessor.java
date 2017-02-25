package cz.codingmonkey.ibs.user.impl;

import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import cz.codingmonkey.ibs.user.impl.data.MyDatabase;
import cz.codingmonkey.ibs.user.impl.domain.ClientEvent;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

/**
 * @author Richard Stefanca
 */
public class ClientEventProcessor extends ReadSideProcessor<ClientEvent> {

	private final MyDatabase myDatabase;

	private final JdbcReadSide jdbcReadSide;

	@Inject
	public ClientEventProcessor(MyDatabase myDatabase, JdbcReadSide jdbcReadSide) {
		this.myDatabase = myDatabase;
		this.jdbcReadSide = jdbcReadSide;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadSideHandler<ClientEvent> buildHandler() {
		JdbcReadSide.ReadSideHandlerBuilder<ClientEvent> builder = jdbcReadSide
				.builder("clientoffset");

		builder.setGlobalPrepare(myDatabase::createTables);
		builder.setEventHandler(ClientEvent.ClientCreated.class, myDatabase::createClient);
		builder.setEventHandler(ClientEvent.ClientDeactivated.class, myDatabase::deactivateClient);

		return builder.build();
	}

	@Override
	public PSequence<AggregateEventTag<ClientEvent>> aggregateTags() {
		return TreePVector.singleton(ClientEvent.CLIENT_EVENT_TAG);
	}
}
