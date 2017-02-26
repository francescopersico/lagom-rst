package cz.codingmonkey.ibs.user.impl;

import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import cz.codingmonkey.ibs.user.impl.data.ClientsDatabase;
import cz.codingmonkey.ibs.user.impl.domain.ClientEvent;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

/**
 * @author Richard Stefanca
 */
public class ClientEventProcessor extends ReadSideProcessor<ClientEvent> {

	private final ClientsDatabase clientsDatabase;

	private final JdbcReadSide jdbcReadSide;

	@Inject
	public ClientEventProcessor(ClientsDatabase clientsDatabase, JdbcReadSide jdbcReadSide) {
		this.clientsDatabase = clientsDatabase;
		this.jdbcReadSide = jdbcReadSide;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadSideHandler<ClientEvent> buildHandler() {
		JdbcReadSide.ReadSideHandlerBuilder<ClientEvent> builder = jdbcReadSide
				.builder("clientoffset");

		builder.setGlobalPrepare(clientsDatabase::createTables);
		builder.setEventHandler(ClientEvent.ClientCreated.class, clientsDatabase::createClient);
		builder.setEventHandler(ClientEvent.ClientDeactivated.class, clientsDatabase::deactivateClient);

		return builder.build();
	}

	@Override
	public PSequence<AggregateEventTag<ClientEvent>> aggregateTags() {
		return TreePVector.singleton(ClientEvent.CLIENT_EVENT_TAG);
	}
}
