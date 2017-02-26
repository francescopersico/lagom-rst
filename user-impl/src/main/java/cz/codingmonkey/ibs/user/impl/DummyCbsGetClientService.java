package cz.codingmonkey.ibs.user.impl;

import cz.codingmonkeys.cbs.api.CbsClient;
import cz.codingmonkeys.cbs.api.CbsClientService;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * @author rstefanca
 */
public class DummyCbsGetClientService implements CbsClientService {
	@Override
	public CompletableFuture<CbsClient> getCbsClient(String externalClientId) {
		return CompletableFuture.completedFuture(new CbsClient(externalClientId, "sms", "email", Collections.emptyList()));
	}
}
