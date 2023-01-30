package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import application.model.User;

public class UserDaoImpl implements UserDao {
	private final String TABLE_NAME = "users";

	public UserDaoImpl() {
	}

	
	/**
	 * Initialize database if table doesn't exist
	 */
	@Override
	public void setup() throws SQLException {
		try (Connection connection = Database.getConnection(); Statement stmt = connection.createStatement();) {
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(username VARCHAR(20) NOT NULL,"
					+ "password VARCHAR(20) NOT NULL," + "firstname VARCHAR(20) NOT NULL,"
					+ "lastname VARCHAR(20) NOT NULL," + "picture BLOB," + "premium INTEGER," + "PRIMARY KEY (username))";
			stmt.executeUpdate(sql);
		}
	}

	
	/**
	 * Compare login information from user.
	 */
	@Override
	public User loginUser(String username, String password) throws SQLException {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			stmt.setString(2, password);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = User.getInstance();
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setFirstName(rs.getString("firstname"));
					user.setLastName(rs.getString("lastname"));
					user.setProfilePic(rs.getBytes("picture"));
					return user;
				}
				return null;
			}
		}
	}

	
	/**
	 * Check if a username already exists in the database
	 */
	@Override
	public boolean checkUserExists(String username) throws SQLException {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = User.getInstance();
					user.setUsername(rs.getString("username"));
					return true;
				}
				return false;
			}
		}
	}

	
	/**
	 * Create a new user in the database and set the singleton user.
	 */
	@Override
	public User createUser(String username, String password, String firstname, String lastname, byte[] profileImage) throws SQLException {
		String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, 0)";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, firstname);
			stmt.setString(4, lastname);
			stmt.setBytes(5, profileImage);
			stmt.executeUpdate();

			// Set user singleton
			User user = User.getInstance();
			user.setUsername(username);
			return user;
		}
	}

	
	/**
	 * Update user details
	 */
	@Override
	public User updateUser(String username, String firstname, String lastname, byte[] profileImage) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET firstname = ?, lastname = ?, picture = ? WHERE username = ?";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, firstname);
			stmt.setString(2, lastname);
			stmt.setBytes(3, profileImage);
			stmt.setString(4, username);
			stmt.executeUpdate();
			return null;
		}
	}

	
	/**
	 * Get user details from database
	 */
	@Override
	public User getUser(String username) throws SQLException {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = User.getInstance();
					user.setUsername(rs.getString("username"));
					user.setFirstName(rs.getString("firstname"));
					user.setLastName(rs.getString("lastname"));
					return user;
				}
				return null;
			}
		}
	}
	
	
	/**
	 * Change user's premium membership status.
	 */
	@Override
	public User updateMembership(String username, int isPremium) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET premium = ? WHERE username = ?";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setInt(1, isPremium);
			stmt.setString(2, username);
			stmt.executeUpdate();
			return null;
		}
	}
}
