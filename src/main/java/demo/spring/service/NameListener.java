package demo.spring.service;

public interface NameListener {

	/**
	 * Callback fired when a name has been given for an input of names.
	 * @param generated The name that was generated
	 * @param firstName The first name provided
	 * @param lastName The last name provided
	 */
	void newNameGiven(String generated, String firstName, String lastName);
	
}
