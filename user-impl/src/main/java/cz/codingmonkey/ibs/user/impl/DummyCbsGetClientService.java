package cz.codingmonkey.ibs.user.impl;

import cz.codingmonkeys.cbs.api.CbsClient;
import cz.codingmonkeys.cbs.api.CbsClientService;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;

/**
 * @author rstefanca
 */
public class DummyCbsGetClientService implements CbsClientService {

	private Random random = new Random();

	@Override
	public CompletableFuture<CbsClient> getCbsClient(String externalClientId) {
		return CompletableFuture.completedFuture(new CbsClient(
				externalClientId,
				"sms",
				"email",
				random.nextBoolean(),
				emptyList()));
	}
}