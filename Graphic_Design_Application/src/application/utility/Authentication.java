package application.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Authentication class contains the logic for log in/log out operations including password hashing.
 */
public class Authentication {

	/**
	 * Generate MD5 has from user's password.
	 */
	public String hashPassword(String password) {
		String hash = null;

		try {
			// Create MessageDigest instance for MD5
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			byte[] bytes = md5.digest();

			// Convert byte array to hex format.
			StringBuilder hex = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				hex.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			// Get complete hashed password in hex format
			hash = hex.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}
}
