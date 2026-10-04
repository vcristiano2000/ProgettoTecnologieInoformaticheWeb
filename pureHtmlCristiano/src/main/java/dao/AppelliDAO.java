package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Appello;


/**
 *  DAO to access appelli's information on the DB
 */
public class AppelliDAO {
	
	private Connection connection;

	/**
	 * Initialize DAO for the given DB connection
	 */
	public AppelliDAO(Connection connection) {
		this.connection = connection;
	}
	

	/**
	 * Returns the list of courses of the idDocente.
	 */
	public List<Appello> getProfAppelli(int idCorso) throws SQLException {

		String action = "Asking Appelli. idCorso: " + idCorso ;
		String query = "SELECT * FROM appelli a WHERE a.id_corso = ? ORDER BY a.data DESC";
		List<Appello> appelli = new ArrayList<Appello>();
		Appello appello;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idCorso);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				appello = new Appello();
				appello.setIdCorso(resultSet.getInt("id_corso"));
				appello.setData(resultSet.getDate("data"));
				appelli.add(appello);
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return appelli;
	}
	/**
	 * Returns the list of courses of the matricola.
	 */
	public List<Appello> getStudAppelli(int idCorso, int matricola) throws SQLException {

		String action = "Asking appelli. id_corso: " + idCorso;
		String query = "SELECT a.id_corso, a.data FROM appelli a JOIN iscritti i ON a.id_corso=i.id_corso AND a.data=i.data WHERE i.matricola = ? AND a.id_corso = ? ORDER BY a.data DESC";
		List<Appello> appelli = new ArrayList<Appello>();
		Appello appello;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, matricola);
			preparedStatement.setInt(2, idCorso);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				appello = new Appello();
				appello.setIdCorso(resultSet.getInt("id_corso"));
				appello.setData(resultSet.getDate("data"));
				appelli.add(appello);
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return appelli;
	}
	
	public List<Date> getDateAppelli(int idCorso) throws SQLException {

		String action = "Asking date appelli. id_corso: " + idCorso;
		String query = "SELECT a.data FROM appelli a WHERE a.id_corso = ?";
		List<Date> date = new ArrayList<>();
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idCorso);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Date data;
				data=resultSet.getDate("data");
				date.add(data);
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return date;
	}
	
}