package cz.codingmonkey.ibs.user.impl;

import com.lightbend.lagom.javadsl.api.transport.TransportErrorCode;
import com.lightbend.lagom.javadsl.api.transport.TransportException;

/**
 * @author rstefanca
 */
class ClientStateException extends TransportException {

	ClientStateException(String s) {
		super(TransportErrorCode.BadRequest, s);
	}
}
