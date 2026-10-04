package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Appello;
import beans.Iscritto;

/**
 *  DAO to access iscritti's information on the DB
 */
public class IscrittiDAO {
	
	public enum Ordering{
		
		MATRICOLA_DESC("MATRICOLA_DESC"),
		MATRICOLA_ASC("MATRICOLA_ASC"),
		NOME_DESC("NOME_DESC"),
		NOME_ASC("NOME_ASC"),
		COGNOME_DESC("COGNOME_DESC"),
		COGNOME_ASC("COGNOME_ASC"),
		EMAIL_DESC("EMAIL_DESC"),
		EMAIL_ASC("EMAIL_ASC"),
		CORSODILAUREA_DESC("CORSODILAUREA_DESC"),
		CORSODILAUREA_ASC("CORSODILAUREA_ASC"),
		VOTO_DESC("VOTO_DESC"),
		VOTO_ASC("VOTO_ASC"),
		STATOVALUTAZIONE_DESC("STATOVALUTAZIONE_DESC"),
		STATOVALUTAZIONE_ASC("STATOVALUTAZIONE_ASC");
		
	private String order;
		    
	Ordering(String order) {
		this.order=order;
		}

	public String getOrder() {
		return order;
		}
	
	public static Ordering getFromString(String string) {
	      for (Ordering type : Ordering.values()) {
	        if (type.getOrder().equals(string))
	          return type;
	      }
	      return null;
	    }
	}
	
	private Connection connection;

	/**
	 * Initialize DAO for the given DB connection
	 */
	public IscrittiDAO(Connection connection) {
		this.connection = connection;
	}
	

	/**
	 * Returns the list of Iscritti.
	 */
	public List<Iscritto> getIscritti(int idCorso, Date data, Ordering ord ) throws SQLException {

		String action = "Asking Iscritti. idCorso: " + idCorso + " data: " + data + " ord: " + ord ;
		String query = "SELECT * FROM iscritti i WHERE i.id_corso = ? AND i.data= ? ORDER BY ";
		switch (ord) {
			case MATRICOLA_DESC: {
				query+="i.matricola DESC";
				break;
			}
			case MATRICOLA_ASC:{
				query+="i.matricola ASC";
				break;
			}
			case COGNOME_DESC:{
				query+="i.cognome DESC";
				break;
			}
			case COGNOME_ASC:{
				query+="i.cognome ASC";
				break;
			}
			case NOME_DESC:{
				query+="i.nome DESC";
				break;
			}
			case NOME_ASC:{
				query+="i.nome ASC";
				break;
			}
			case EMAIL_DESC:{ 
				query+="i.email DESC";
				break;
			}
			case EMAIL_ASC: {
				query+="i.email ASC";
				break;
			}
			case CORSODILAUREA_DESC: {
				query+="i.corso_di_laurea DESC";
				break;
			}
			case CORSODILAUREA_ASC:{
				query+="i.corso_di_laurea ASC";
				break;
			}
			case VOTO_DESC: {
				query+="i.voto DESC";
				break;
			}
			case VOTO_ASC: {
				query+="i.voto ASC";
				break;
			}
			case STATOVALUTAZIONE_DESC: {
				query+="i.stato_della_valutazione DESC";
				break;
			}
			case STATOVALUTAZIONE_ASC: {
				query+="i.stato_della_valutazione ASC";
				break;
			}
			default: query+="i.matricola ASC";
		}
		List<Iscritto> iscritti = new ArrayList<Iscritto>();
		Iscritto iscritto;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idCorso);
			preparedStatement.setDate(2, data);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				iscritto = new Iscritto();
				iscritto.setMatricola(resultSet.getInt("matricola"));
				iscritto.setIdCorso(resultSet.getInt("id_corso"));
				iscritto.setData(resultSet.getDate("data"));
				iscritto.setCognome(resultSet.getString("cognome"));
				iscritto.setNome(resultSet.getString("nome"));
				iscritto.setEmail(resultSet.getString("email"));
				iscritto.setCorsoDiLaurea(resultSet.getString("corso_di_laurea"));
				iscritto.setVoto(resultSet.getString("voto"));
				iscritto.setStatoDellaValutazione(resultSet.getString("stato_della_valutazione"));
				iscritti.add(iscritto);
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return iscritti;
	}
	
		public void ModificaVoto(String voto, int idCorso, Date data, int matricola) throws SQLException {
		
			String action = "changing vote of matricola: " + matricola + " data: " + data + "idCorso: " + idCorso;
			String update = "UPDATE iscritti SET voto = ?, stato_della_valutazione = 'inserito' WHERE id_corso = ? AND data = ? AND matricola = ?";
		
			try (PreparedStatement preparedStatement = connection.prepareStatement(update)){
				preparedStatement.setString(1, voto);
				preparedStatement.setInt(2, idCorso);
				preparedStatement.setDate(3, data);
				preparedStatement.setInt(4, matricola);
				preparedStatement.executeUpdate();
			}catch(SQLException e) {
				throw new SQLException("DB ACCESS ERROR. Action: " + action);
			}
		}
		
		public void pubblicaVoti(int idCorso, Date data) throws SQLException {
			
			String action = "publishing votes of idCorso: " + idCorso + " data: " + data;
			String update = "UPDATE iscritti SET stato_della_valutazione = 'pubblicato' WHERE id_corso = ? AND data = ? AND stato_della_valutazione = 'inserito'";
		
			try (PreparedStatement preparedStatement = connection.prepareStatement(update)){
				preparedStatement.setInt(1, idCorso);
				preparedStatement.setDate(2, data);
				preparedStatement.executeUpdate();
			}catch(SQLException e) {
				throw new SQLException("DB ACCESS ERROR. Action: " + action);
			}
		}
	
		public List<Iscritto> getStudentiDaVerbalizzare(Appello appello) throws SQLException {
			
			String action = "Picking students";
			String query = "SELECT * FROM iscritti WHERE id_corso = ? AND data = ? AND (stato_della_valutazione = 'pubblicato' OR stato_della_valutazione = 'rifiutato') ";
			List<Iscritto> studenti = new ArrayList<Iscritto>();
			Iscritto studente;
			
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
				preparedStatement.setInt(1, appello.getIdCorso());
				preparedStatement.setDate(2, appello.getData());
				ResultSet resultSet = preparedStatement.executeQuery();
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
				} 
			}catch(SQLException e) {
				throw new SQLException("DB ACCESS ERROR. Action: " + action);
			}
			
			return studenti;
		}
		
	public void updateStatoVerbalizzazioneIscritto(Iscritto s) throws SQLException {
		
		String action = "changing the status of the student S assessment";
		String update = "UPDATE iscritti i SET stato_della_valutazione = 'verbalizzato' WHERE i.matricola = ? AND i.id_corso = ? AND i.data = ? ";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(update)){
			preparedStatement.setInt(1, s.getMatricola());
			preparedStatement.setInt(2, s.getIdCorso());
			preparedStatement.setDate(3, s.getData());
			preparedStatement.executeUpdate();
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
	}
	
	public void RifiutaVoto(int idCorso, Date data, int matricola) throws SQLException {
		
		String action = "refusing the vote";
		String update = "UPDATE iscritti SET voto = 'rimandato', stato_della_valutazione = 'rifiutato' WHERE id_corso = ? AND data = ? AND matricola = ?";
	
		try (PreparedStatement preparedStatement = connection.prepareStatement(update)){
			preparedStatement.setInt(1, idCorso);
			preparedStatement.setDate(2, data);
			preparedStatement.setInt(3, matricola);
			preparedStatement.executeUpdate();
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
	}
	
	public List<Iscritto> getStudentiVerbale(Appello appello) throws SQLException {
		
		String action = "Picking students";
		String query = "SELECT * FROM iscritti WHERE id_corso = ? AND data = ? AND (stato_della_valutazione = 'pubblicato' OR stato_della_valutazione = 'rifiutato') ";
		List<Iscritto> studenti = new ArrayList<Iscritto>();
		Iscritto studente;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, appello.getIdCorso());
			preparedStatement.setDate(2, appello.getData());
			ResultSet resultSet = preparedStatement.executeQuery();
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
			} 
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return studenti;
	}
	
	public List<Integer> getStudentIdCourses(int matricola) throws SQLException {
		String action = "Asking StudentIdCourses. matricola: " + matricola;
		String query = "SELECT i.id_corso FROM iscritti i WHERE i.matricola = ?";
		List<Integer> idCorsi = new ArrayList<>();
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, matricola);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int idCorso;
				idCorso =(resultSet.getInt("id_corso"));
				idCorsi.add(idCorso);
			}
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return idCorsi;
		
	}

	public boolean checkIscrittoVoteIsChangeable(int idCorso, Date data, int matricola) throws SQLException {
		String action = "Checking if vote is changeable";
		String query = "SELECT i.stato_della_valutazione FROM iscritti i WHERE i.id_corso = ? AND i.data = ? AND i.matricola = ?";
		boolean isChangeable = true;
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
			preparedStatement.setInt(1, idCorso);
			preparedStatement.setDate(2, data);
			preparedStatement.setInt(3, matricola);
			ResultSet resultSet = preparedStatement.executeQuery();
			String statoDellaValutazione = "inserito";
			if (resultSet.next()) {
				statoDellaValutazione =(resultSet.getString("stato_della_valutazione"));
			}else {
				isChangeable = false;
			}
			if (!statoDellaValutazione.equals("non_inserito") && !statoDellaValutazione.equals("inserito")) {
				isChangeable = false;
			}
		}catch(SQLException e) {
			throw new SQLException("DB ACCESS ERROR. Action: " + action);
		}
		
		return isChangeable;
		
	}
	
}