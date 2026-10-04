package beans;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String user;
	private boolean professore;
	private int matricola;
	private int idDocente;
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public void setUser(String user)
	{
		this.user=user;
	}
	
	public void setProfessore(boolean professore)
	{
		this.professore=professore;
	}
	
	public void setMatricola(int matricola)
	{
		this.matricola=matricola;
	}
	
	public void setIdDocente(int idDocente)
	{
		this.idDocente=idDocente;
	}
	
	public int getId() {
		return id;
	}
	
	
	public String getUser() {
		return user;
	}
	
	public boolean isProfessore() {
		return professore;
	}
	
	public int getMatricola() {
		return matricola;
	}
	
	public int getIdDocente() {
		return idDocente;
	}
	
	
} 