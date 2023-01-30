package unitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import org.junit.Test;
import org.sqlite.SQLiteException;
import application.utility.Authentication;
import application.dao.UserDaoImpl;


public class applicationTestCases {

	// Test if MD5 hashing returns the expected result.
	@Test
	public void testCalculateMD5_Hash() {
		Authentication auth = new Authentication();
		String actual = auth.hashPassword("test");
		String expected = "098f6bcd4621d373cade4e832627b4f6";
		assertEquals(expected, actual);
	}
	
	// Test if primary key in SQLite DB stops the same user from being created.
	@Test (expected = SQLiteException.class)
	public void testSQLiteDB_DuplicateUser() throws SQLException, SQLiteException {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		byte[] test = null;
		assertEquals("application.model.User@77a57272", userDaoImpl.createUser("username", "password", "firstName", "lastName", test));
	}
	
	// Test if loginUser method returns the expected user.
	@Test
	public void testloginUser_ExpectedHashCode() throws SQLException, SQLiteException {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		int actual = userDaoImpl.loginUser("username", "password").hashCode();
		int expected = 1825027294;
		assertEquals(expected, actual);
	}
	
	// Test if checkUserExists method returns the expected user.
	@Test
	public void testCheckUserExists_ExpectedUser() throws SQLException, SQLiteException {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		boolean actual = userDaoImpl.checkUserExists("username");
		boolean expected = true;
		assertEquals(expected, actual);
	}
	
}











