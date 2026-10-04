package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.Esito;
import beans.Iscritto;


/**
 *  DAO to access voti's information on the DB
 */
public class EsitoDAO {
	
	private Connection connection;

	/**
	 * Initialize DAO for the given DB connection
	 */
	public EsitoDAO(Connection connection) {
		this.connection = connection;
	}
	

	/**
	 * Returns the list of courses of the idDocente.
	 */
	public Esito getEsito (int idCorso, Date data, int matricola) throws SQLException {
		
		String action = "Asking esito. appello: " + idCorso + "-> " + data + "matricola" + matricola ;
		String query = "SELECT * FROM iscritti i JOIN corso c ON i.id_corso=c.id_corso WHERE i.id_corso = ? AND i.data = ? AND i.matricola = ?";
		Esito esito = new Esito();
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idCorso);
			preparedStatement.setDate(2, data);
			preparedStatement.setInt(3, matricola);
			ResultSet resultSet = preparedStatement.executeQuery();
			Iscritto studente;
			if (resultSet.next()) {
				studente = new Iscritto();
				studente.setMatricola(resultSet.getInt("matricola"));
				studente.setIdCorso(resultSet.getInt("id_corso"));
				studente.setData(resultSet.getDate("data"));
				studente.setCognome(resultSet.getString("cognome"));
				studente.setNome(resultSet.getString("nome"));
				studente.setEmail(resultSet.getString("email"));
				studente.setCorsoDiLaurea(resultSet.getString("corso_di_laurea"));
				studente.setVoto(resultSet.getString("voto"));
				studente.setStatoDellaValutazione(resultSet.getString("stato_della_valutazione"));
				esito.setStudente(studente);
				esito.setIdDocente(resultSet.getInt("id_docente"));
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return esito;
	}
	
	
}