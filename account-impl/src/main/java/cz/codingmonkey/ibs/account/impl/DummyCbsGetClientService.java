package cz.codingmonkey.ibs.account.impl;

import cz.codingmonkeys.cbs.api.CbsAccount;
import cz.codingmonkeys.cbs.api.CbsClient;
import cz.codingmonkeys.cbs.api.CbsClientService;

import java.util.concurrent.CompletableFuture;

import static com.google.common.collect.ImmutableList.of;

/**
 * @author rstefanca
 */
@Deprecated
public class DummyCbsGetClientService implements CbsClientService {
	@Override
	public CompletableFuture<CbsClient> getCbsClient(String externalClientId) {
		return CompletableFuture.completedFuture(
				new CbsClient(
						externalClientId,
						"sms",
						"email",
						of(new CbsAccount("iban1"),
								new CbsAccount("iban2"))));
	}
}
