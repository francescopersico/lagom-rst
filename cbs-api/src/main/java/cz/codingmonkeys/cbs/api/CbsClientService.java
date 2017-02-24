package cz.codingmonkeys.cbs.api;

import java.util.concurrent.CompletableFuture;

/**
 * @author rstefanca
 */
public interface CbsClientService {

	CompletableFuture<CbsClient> getCbsClient(String externalClientId);
}
