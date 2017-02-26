package cz.codingmonkey.ibs.account.impl.domain;

import com.lightbend.lagom.javadsl.api.transport.TransportErrorCode;
import com.lightbend.lagom.javadsl.api.transport.TransportException;

/**
 * @author rstefanca
 */
public class OverdrawException extends TransportException {

	public OverdrawException(String message) {
		super(TransportErrorCode.PolicyViolation, message);
	}
}
