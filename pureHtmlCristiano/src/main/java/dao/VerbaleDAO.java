package dao;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Statement;

import beans.Appello;
import beans.Iscritto;
import beans.Verbale;

/**
 *  DAO to access verbale information on the DB
 */
public class VerbaleDAO {
	
	private Connection connection;

	/**
	 * Initialize DAO for the given DB connection
	 */
	public VerbaleDAO(Connection connection) {
		this.connection = connection;
	}
	

	/**
	 * Returns the list of courses of the idDocente.
	 */
	public Verbale verbalizza(Appello appello) throws SQLException {
		
		int idVerbale = getMaxId() + 1;
		
		IscrittiDAO iscrittiDAO = new IscrittiDAO(connection);
		List<Iscritto> studenti = iscrittiDAO.getStudentiDaVerbalizzare(appello);
		System.out.println("studenti" + studenti);
		Verbale verbale = new Verbale();
		verbale.setIdVerbale(idVerbale);
		System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		verbale.setTimestamp(timestamp);
		verbale.setIscritti(studenti);
		
		
		try {
			connection.setAutoCommit(false);
			for (Iscritto s: studenti) {	
				iscrittiDAO.updateStatoVerbalizzazioneIscritto(s);
				insertVerbaleStudente(idVerbale,s.getIdCorso(),timestamp, s.getData(), s.getMatricola());
			}
			connection.commit();
		}catch (SQLException e) {
			connection.rollback();
			throw new SQLException("DB ACCESS ERROR failed to inesrt student in verbale" + e.getMessage());
		} finally {
			connection.setAutoCommit(true);
		}
		return verbale; 
	}
	 
	 public int getMaxId() throws SQLException  {
		String action = "Asking max of idVerbale" ;
		int idVerbale=0;
		String query = "SELECT MAX(id_verbale) id_verbale FROM verbali";
		try(Statement statement = connection.createStatement()){
			action+=statement;
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet.next()) {
				idVerbale = resultSet.getInt("id_verbale");
			}
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		return idVerbale;
	 }
	 
		public void insertVerbaleStudente(int idVerbale, int idCorso, Timestamp dataEOra , Date data, int matricola) throws SQLException {
			
			String action = "Creating verbale studente with matricola: " + matricola;
			String query = "INSERT INTO verbali(id_verbale, data_e_ora, id_corso, data_appello, matricola) VALUES(?,?,?,?,?)";
			
			try (PreparedStatement preparedStatementQuery = connection.prepareStatement(query))
			{
				preparedStatementQuery.setInt(1, idVerbale);
				preparedStatementQuery.setTimestamp(2, dataEOra);
				preparedStatementQuery.setInt(3, idCorso);
				preparedStatementQuery.setDate(4, data);
				preparedStatementQuery.setInt(5, matricola);
				preparedStatementQuery.executeUpdate();
				
			} catch(SQLException e) {
		
				throw new SQLException("DB ACCESS ERROR. Action: " + action + e.getMessage());
			} 
		}
	
		public Verbale getVerbale(int idVerbale) throws SQLException {
			
			String action = "Picking verbale";
			String query = "SELECT DISTINCT i.matricola, i.id_corso, i.data, i.cognome, i.nome, i.email, i.corso_di_laurea, i.voto, i.stato_della_valutazione, v.data_e_ora  FROM verbali v JOIN iscritti i ON v.data_appello=i.data AND v.id_corso=i.id_corso AND v.matricola=i.matricola WHERE id_verbale = ? ";
		    
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
				preparedStatement.setInt(1, idVerbale);
				ResultSet resultSet = preparedStatement.executeQuery();
				Iscritto studente;
			    List<Iscritto> studenti = new ArrayList<>();
			    Verbale verbale = new Verbale();
				while (resultSet.next()) {
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
					studenti.add(studente);
					verbale.setTimestamp(resultSet.getTimestamp("data_e_ora"));
				}
				verbale.setIdVerbale(idVerbale);
				verbale.setIscritti(studenti); 
				return verbale;
			}catch(SQLException e) {
				throw new SQLException("DB ACCESS ERROR. Action: " + action + e.getMessage());
			}
			
			
		}
}