package beans;



public class Esito {
	
	private Iscritto studente;
	private int idDocente;
		
	public void setStudente(Iscritto studente)
	{
		this.studente=studente;
	}
	
	public void setIdDocente(int idDocente)
	{
		this.idDocente=idDocente;
	}
	
	public Iscritto getStudente() {
		return studente;
	}
	
	public int getIdDocente() {
		return idDocente;
	}
	
	
} 