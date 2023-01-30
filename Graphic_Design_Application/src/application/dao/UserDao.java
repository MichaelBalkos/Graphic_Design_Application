package application.dao;


import java.sql.SQLException;
import application.model.User;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface 
 * to a database or other persistence mechanism. 
 * the DAO maps application calls to the persistence layer and provides some specific data operations 
 * without exposing details of the database. 
 */
public interface UserDao {
	void setup() throws SQLException;
	User loginUser(String username, String password) throws SQLException;
	User getUser(String username) throws SQLException;
	boolean checkUserExists(String username) throws SQLException;
	User createUser(String username, String password, String firstName, String lastName, byte[] profileImage) throws SQLException;
	User updateUser(String username, String firstName, String lastName, byte[] bs) throws SQLException;
	User updateMembership(String username, int isPremium) throws SQLException;
}
