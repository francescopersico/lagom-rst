package cz.codingmonkeys.cbs.api;

/**
 * @author Richard Stefanca
 */
public class CbsAccount {

	private final String iban;

	public CbsAccount(String iban) {
		this.iban = iban;
	}

	public String getIban() {
		return iban;
	}
}
