package cz.codingmonkey.financialtransaction.impl.domain;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;
import cz.codingmonkey.financialtransaction.api.Transaction;
import cz.codingmonkey.financialtransaction.api.TxState;
import cz.codingmonkey.financialtransaction.impl.domain.TransactionCommand.CreateTransaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static cz.codingmonkey.financialtransaction.impl.domain.TransactionEvent.TransactionCreated;
import static org.junit.Assert.assertEquals;

/**
 * @author rstefanca
 */
@SuppressWarnings("unchecked")
public class TransactionEntityTest {

	private static ActorSystem system;

	@BeforeClass
	public static void setup() {
		system = ActorSystem.create();
	}

	@AfterClass
	public static void teardown() {
		JavaTestKit.shutdownActorSystem(system);
		system = null;
	}

	@Test
	public void testCreateTransaction() throws Exception {
		PersistentEntityTestDriver<TransactionCommand, TransactionEvent, TransactionState> driver =
				new PersistentEntityTestDriver<>(system, new TransactionEntity(), "tx-1");

		Transaction transaction = Transaction.builder().clientId("clientId").build();
		CreateTransaction command = new CreateTransaction(transaction);

		Outcome<TransactionEvent, TransactionState> outcome = driver.run(command);

		assertEquals(new TransactionCreated("tx-1", transaction), outcome.events().get(0));
		assertEquals(1, outcome.events().size());

		assertEquals(TxState.CREATED, outcome.state().getState());
		assertEquals(Optional.of(transaction), outcome.state().getTransaction());
		assertEquals(Done.getInstance(), outcome.getReplies().get(0));
		//assertEquals(Collections.emptyList(), outcome.issues());

	}
}