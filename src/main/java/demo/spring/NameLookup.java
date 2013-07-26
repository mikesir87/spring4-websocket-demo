package demo.spring;

public class NameLookup {

	private final String firstName;
	private final String lastName;
	private final String nameType;
	private final String newName;
	
	public NameLookup(String firstName, String lastName, String nameType, 
			String newName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nameType = nameType;
		this.newName = newName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getNameType() {
		return nameType;
	}
	
	public String getNewName() {
		return newName;
	}
	
}
