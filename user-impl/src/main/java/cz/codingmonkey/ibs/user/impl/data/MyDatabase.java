package cz.codingmonkey.ibs.user.impl.data;

import cz.codingmonkey.ibs.user.impl.domain.ClientEvent;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author rstefanca
 */
public interface MyDatabase {

	void createTables(Connection connection) throws SQLException;

	void createClient(Connection connection, ClientEvent.ClientCreated e) throws SQLException;

	void deactivateClient(Connection connection, ClientEvent.ClientDeactivated e) throws SQLException;
}
