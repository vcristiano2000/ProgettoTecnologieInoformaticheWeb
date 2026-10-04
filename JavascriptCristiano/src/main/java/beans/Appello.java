package beans;
import java.io.Serializable;
import java.sql.Date;

public class Appello implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int idCorso;
	private String dataString;
	private Date data;
		
	public void setIdCorso(int idCorso)
	{
		this.idCorso=idCorso;
	}
	
	public void setData (Date data)	{
		this.data=data;
	}
	
	public void setDataString (String dataString)	{
		this.dataString = dataString;
	}
	
	public int getIdCorso() {
		return idCorso;
	}
	
	public Date getData() {
		return data;
	}
	
	public String getDataString() {
		return dataString;
	}
	
} 