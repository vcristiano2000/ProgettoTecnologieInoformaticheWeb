package beans;
import java.sql.Date;

public class Appello {
	
	private int idCorso;
	private Date data;
		
	public void setIdCorso(int idCorso)
	{
		this.idCorso=idCorso;
	}
	
	public void setData (Date data)	{
		this.data=data;
	}
	
	public int getIdCorso() {
		return idCorso;
	}
	
	
	public Date getData() {
		return data;
	}
	
	
} 