package cz.codingmonkey.ibs.account.impl;

import cz.codingmonkeys.cbs.api.CbsAccount;
import cz.codingmonkeys.cbs.api.CbsClient;
import cz.codingmonkeys.cbs.api.CbsClientService;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.google.common.collect.ImmutableList.of;

/**
 * @author rstefanca
 * @deprecated only for testing
 */
@Deprecated
public class DummyCbsGetClientService implements CbsClientService {

	private Random random = new Random();

	@Override
	public CompletableFuture<CbsClient> getCbsClient(String externalClientId) {
		return CompletableFuture.completedFuture(
				new CbsClient(
						externalClientId,
						"sms",
						"email",
						of(new CbsAccount("iban1-" + Math.abs(random.nextLong())),
								new CbsAccount("iban2-" + Math.abs(random.nextLong())))));
	}
}
