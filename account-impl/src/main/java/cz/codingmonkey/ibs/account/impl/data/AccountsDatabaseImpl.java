package cz.codingmonkey.ibs.account.impl.data;

import cz.codingmonkey.ibs.account.impl.domain.AccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author rstefanca
 */
@Slf4j
public class AccountsDatabaseImpl implements AccountsDatabase {

	private final QueryRunner queryRunner = new QueryRunner();

	@Override
	public void createTables(Connection connection) throws SQLException {
		log.info("creating tables");
		try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS accounts ( " +
				"iban VARCHAR(64), name VARCHAR(256), clientId VARCHAR(64), PRIMARY KEY (iban))")) {
			ps.execute();
		}

		try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS movements(" +
				"id VARCHAR(64), iban VARCHAR(64), otherIban VARCHAR(64), amount NUMBER(19,2), PRIMARY KEY (id))")) {
			ps.execute();
		}

		//set fk movements.iban -> accounts.iban
	}

	@Override
	public void createAccount(Connection connection, AccountEvent.AccountAdded evt) throws SQLException {
		log.info("handling db create account");
		int updated = queryRunner.update(
				connection,
				"INSERT INTO accounts(iban, name, clientId) values (?,?,?)",
				evt.iban, "todoName", evt.clientId);

		log.info("{} rows updated", updated);
	}

	@Override
	public void addMovement(Connection connection, AccountEvent.MoneyTransferred evt) throws SQLException {
		log.info("handling db movement: {}", evt.paymentId);

		int updated = queryRunner.update(
				connection,
				"INSERT INTO movements(id, iban, otherIban, amount) values(?,?,?,?)",
				evt.paymentId, evt.iban, evt.movement.getOtherIban(), evt.movement.getAmount().floatValue());

		log.info("{} rows updated", updated);
	}
}
