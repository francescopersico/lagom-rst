package cz.codingmonkey.ibs.account.impl.data;

import cz.codingmonkey.ibs.account.impl.domain.AccountEvent;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author rstefanca
 */
public interface AccountsDatabase {

	void createTables(Connection connection) throws SQLException;

	void createAccount(Connection connection, AccountEvent.AccountAdded evt) throws SQLException;

	void addMovement(Connection connection, AccountEvent.MoneyTransferred evt) throws SQLException;
}
