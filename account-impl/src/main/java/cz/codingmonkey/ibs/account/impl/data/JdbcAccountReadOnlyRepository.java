package cz.codingmonkey.ibs.account.impl.data;

import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import cz.codinmonkey.ibs.account.api.PaymentInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;

/**
 * @author rstefanca
 */
public class JdbcAccountReadOnlyRepository implements AccountReadOnlyRepository {

	private final JdbcSession jdbcSession;
	private final QueryRunner queryRunner = new QueryRunner();

	@Inject
	public JdbcAccountReadOnlyRepository(JdbcSession jdbcSession) {
		this.jdbcSession = jdbcSession;
	}

	@Override
	public CompletionStage<PSequence<PaymentInfo>> getPaymentsByIban(String iban) {
		return jdbcSession.withConnection(connection -> {
			List<Map<String, Object>> rows = queryRunner.query(connection, "SELECT * FROM movements WHERE iban=?", new MapListHandler(), iban);
			List<PaymentInfo> payments = rows.stream().map(this::buildPayment).collect(toList());
			return TreePVector.from(payments);
		});
	}

	private PaymentInfo buildPayment(Map<String, Object> row) {
		return PaymentInfo.builder()
				.id((String) row.get("id"))
				.iban((String) row.get("iban"))
				.otherIban((String) row.get("otherIban"))
				.amount((BigDecimal)row.get("amount"))
				.build();
	}
}
