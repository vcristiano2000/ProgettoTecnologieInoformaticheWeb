package beans;
import java.sql.Timestamp;
import java.util.List;

public class Verbale {
	
	private int idVerbale;
	private Timestamp timestamp;
	private List<Iscritto> iscritti;
		
	public void setIdVerbale(int idVerbale)
	{
		this.idVerbale=idVerbale;
	}
	
	public void setTimestamp (Timestamp timestamp)	{
		this.timestamp=timestamp;
	}
	
	public void setIscritti(List<Iscritto> iscritti) {
		this.iscritti=iscritti;
	}
	
	public int getIdVerbale() {
		return idVerbale;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public List<Iscritto> getIscritti() {
		return iscritti;
	}
	
	
	
} 