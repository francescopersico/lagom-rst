package cz.codingmonkey.ibs.account.impl.data;

import cz.codinmonkey.ibs.account.api.PaymentInfo;
import org.pcollections.PSequence;

import java.util.concurrent.CompletionStage;

/**
 * @author rstefanca
 */
public interface AccountReadOnlyRepository {

	CompletionStage<PSequence<PaymentInfo>> getPaymentsByIban(String iban);
}
