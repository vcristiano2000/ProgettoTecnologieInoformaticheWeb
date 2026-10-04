package beans;

import java.io.Serializable;

public class Corso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int idCorso;
	private String nomeCorso;
	private int idDocente;
	
	public void setIdCorso(int idCorso)
	{
		this.idCorso=idCorso;
	}
	
	public void setNomeCorso (String nomeCorso)
	{
		this.nomeCorso=nomeCorso;
	}
	
	public void setIdDocente(int idDocente)
	{
		this.idDocente=idDocente;
	}
	
	public int getIdCorso() {
		return idCorso;
	}
	
	
	public String getNomeCorso() {
		return nomeCorso;
	}
	
	public int getIdDocente() {
		return idDocente;
	}
	
	
} 