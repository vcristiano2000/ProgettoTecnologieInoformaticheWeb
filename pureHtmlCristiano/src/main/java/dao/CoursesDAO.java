package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Corso;

/**
 *  DAO to access courses information on the DB
 */
public class CoursesDAO {
	
	private Connection connection;

	/**
	 * Initialize DAO for the given DB connection
	 */
	public CoursesDAO(Connection connection) {
		this.connection = connection;
	}
	

	/**
	 * Returns the list of courses of the idDocente.
	 */
	public List<Corso> getProfCourses(int idDocente) throws SQLException {

		String action = "Asking Courses. idDocoente: " + idDocente ;
		String query = "SELECT * FROM corso c WHERE c.id_docente = ? ORDER BY c.nome_corso DESC";
		List<Corso> corsi = new ArrayList<Corso>();
		Corso corso;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idDocente);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				corso = new Corso();
				corso.setIdCorso(resultSet.getInt("id_corso"));
				corso.setNomeCorso(resultSet.getString("nome_corso"));
				corso.setIdDocente(resultSet.getInt("id_docente"));
				corsi.add(corso);
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return corsi;
	}
	/**
	 * Returns the list of courses of the matricola.
	 */
	public List<Corso> getStudCourses(int matricola) throws SQLException {

		String action = "Asking Courses. matricola: " + matricola;
		String query = "SELECT DISTINCT c.id_corso, c.nome_corso, c.id_docente FROM corso c JOIN iscritti i ON c.id_corso=i.id_corso WHERE i.matricola = ? ORDER BY nome_corso DESC";
		List<Corso> corsi = new ArrayList<Corso>();
		Corso corso;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, matricola);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				corso = new Corso();
				corso.setIdCorso(resultSet.getInt("id_corso"));
				corso.setNomeCorso(resultSet.getString("nome_corso"));
				corso.setIdDocente(resultSet.getInt("id_docente"));
				corsi.add(corso);
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return corsi;
	}
	
	public String getNomeCorso(int idCorso) throws SQLException {
		String action = "Asking CourseName. idCourse: " + idCorso;
		String query = "SELECT c.nome_corso FROM corso c WHERE c.id_corso = ?";
		String nomeCorso = null;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idCorso);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				nomeCorso =(resultSet.getString("nome_corso"));
			}
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return nomeCorso;
		
	}
	
	public int getDocenteCorso(int idCorso) throws SQLException {
		String action = "Asking DocenteName. idCourse: " + idCorso;
		String query = "SELECT c.id_docente FROM corso c WHERE c.id_corso = ?";
		int idDocente=-1;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idCorso);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				idDocente =(resultSet.getInt("id_docente"));
			}
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return idDocente;
		
	}
	
}