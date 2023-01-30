package application.model;

/**
 * The User class stores the details of all users.
 */
public class User {
	
	// Singleton implementation - Restrict instantiation of class from other classes.
	private User() {
	}
	
	/**
	 * Singleton Pattern - Only a single instance of the User class should exist.
	 * Inner helper class for Bill Pugh type implementation.
	 * Does not require synchronization (better performance), yet multithreading safe.
	 */
	private static class InnerSingletonUserClass {
		private static final User user = new User();
	}
	
	/**
	 * Singleton implementation - Public method for other classes to obtain the class instance.
	 */
	public static User getInstance() {
		return InnerSingletonUserClass.user;
	}

	
	// Initialize values.
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private byte[] profilePic;
	
	// Getter and setter methods.
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public byte[] getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}
	
}