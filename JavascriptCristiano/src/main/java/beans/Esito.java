package beans;



public class Esito {
	
	private Iscritto studente;
	private int idDocente;
	private String dataString;
		
	public void setStudente(Iscritto studente)
	{
		this.studente=studente;
	}
	
	public void setIdDocente(int idDocente)
	{
		this.idDocente=idDocente;
	}
	
	public void setDataString (String dataString)	{
		this.dataString = dataString;
	}
	
	
	public Iscritto getStudente() {
		return studente;
	}
	
	public int getIdDocente() {
		return idDocente;
	}
	public String getDataString() {
		return dataString;
	}
	
	
} 