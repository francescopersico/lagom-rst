package cz.codingmonkey.ibs.user.impl.data;

import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import cz.codingmonkey.ibs.user.api.Client;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * @author rstefanca
 */
public class ReadRepositoryImpl implements ReadRepository {

	private final JdbcSession jdbcSession;
	private final QueryRunner queryRunner = new QueryRunner();

	@Inject
	public ReadRepositoryImpl(JdbcSession jdbcSession) {
		this.jdbcSession = jdbcSession;
	}

	@Override
	public CompletionStage<Optional<Client>> getClient(String id) {
		return jdbcSession.withConnection(con -> getClient(con, id));
	}

	@Override
	public CompletionStage<Boolean> clientWithExternalIdExists(String externalClientId) {
		return jdbcSession.withConnection(con ->
				queryRunner.query(
						con,
						"SELECT COUNT(*) FROM clients WHERE externalId=?",
						new ScalarHandler<Long>(),
						externalClientId))
				.thenApply(found -> found > 0);
	}

	private Optional<Client> getClient(Connection connection, String id) throws SQLException {
		List<Map<String, Object>> rows = queryRunner.query(connection, "SELECT * FROM clients WHERE id=?", new MapListHandler(), id);
		return rows.stream().map(ReadRepositoryImpl::buildClient).findAny();
	}

	private static Client buildClient(Map<String, Object> row) {
		return Client.builder()
				.externalId((String) row.get("externalId"))
				.sms((String) row.get("sms"))
				.email((String) row.get("email"))
				.active(true) //todo
				.build();
	}
}
