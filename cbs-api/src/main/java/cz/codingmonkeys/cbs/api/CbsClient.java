package cz.codingmonkeys.cbs.api;

/**
 * @author rstefanca
 */
public class CbsClient {
	private final String externalClientId;
	private final String sms;
	private final String email;

	public CbsClient(String externalClientId, String sms, String email) {

		this.externalClientId = externalClientId;
		this.sms = sms;
		this.email = email;
	}

	public String getExternalClientId() {
		return externalClientId;
	}

	public String getSms() {
		return sms;
	}

	public String getEmail() {
		return email;
	}
}
