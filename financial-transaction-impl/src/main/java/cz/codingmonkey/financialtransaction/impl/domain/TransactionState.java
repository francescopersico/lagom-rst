package cz.codingmonkey.financialtransaction.impl.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lightbend.lagom.serialization.Jsonable;
import cz.codingmonkey.financialtransaction.api.Transaction;
import cz.codingmonkey.financialtransaction.api.TxState;

import java.util.Optional;

/**
 * @author rstefanca
 */

public class TransactionState implements Jsonable {

	static final TransactionState EMPTY = new TransactionState(Optional.empty(), TxState.NOT_CREATED);

	private final Optional<Transaction> transaction;
	private final TxState state;


	@JsonCreator
	public TransactionState(Optional<Transaction> transaction, TxState state) {
		this.transaction = transaction;
		this.state = state;
	}

	public static TransactionState created(Transaction tx) {
		return new TransactionState(Optional.of(tx), TxState.CREATED);
	}

	public TxState getState() {
		return state;
	}

	public Optional<Transaction> getTransaction() {
		return transaction;
	}

	@JsonIgnore
	public boolean isEmpty() {
		return !transaction.isPresent();
	}

}
