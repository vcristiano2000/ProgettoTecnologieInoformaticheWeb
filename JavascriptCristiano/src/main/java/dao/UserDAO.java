package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.User;

/**
 *  DAO to access user information on the DB
 */
public class UserDAO {
	
	private Connection connection;

	/**
	 * Initialize DAO for the given DB connection
	 */
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	

	/**
	 * Check if the id and password correspond to a registered user.
	 * Returns the user if found, or null if it does not exist.
	 */
	public User checkCredentials(int id, String password) throws SQLException {

		String action = "Checking credentials. id: " + id + " password: " + password;
		String query = "SELECT * FROM credentials WHERE id = ? AND password = ?";
		User user = null;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getInt("id"));
				user.setUser(resultSet.getString("user"));
				user.setProfessore(resultSet.getInt("professore")==1);
				if(user.isProfessore()) {
					user.setIdDocente(resultSet.getInt("id_docente"));
				}
				else {
					user.setMatricola(resultSet.getInt("matricola"));
				}
				
				
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		return user;
	}
	
}