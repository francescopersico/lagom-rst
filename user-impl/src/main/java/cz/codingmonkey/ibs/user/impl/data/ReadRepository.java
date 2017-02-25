package cz.codingmonkey.ibs.user.impl.data;

import cz.codingmonkey.ibs.user.api.Client;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * @author Richard Stefanca
 */
public interface ReadRepository {

	CompletionStage<Optional<Client>> getClient(String id);

	CompletionStage<Boolean> clientWithExternalIdExists(String externalClientId);
}
