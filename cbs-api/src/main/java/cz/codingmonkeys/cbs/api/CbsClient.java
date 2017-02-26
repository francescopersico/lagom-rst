package cz.codingmonkeys.cbs.api;

import java.util.List;

/**
 * @author rstefanca
 */
public class CbsClient {
	private final String externalClientId;
	private final String sms;
	private final String email;
	private final List<CbsAccount> accounts;

	public CbsClient(String externalClientId, String sms, String email, List<CbsAccount> accounts) {

		this.externalClientId = externalClientId;
		this.sms = sms;
		this.email = email;
		this.accounts = accounts;
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

	public List<CbsAccount> getAccounts() {
		return accounts;
	}
}
