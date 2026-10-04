package beans;
import java.sql.Date;

public class Iscritto {
	
	private int matricola;
	private int idCorso;
	private Date data;
	private String cognome;
	private String nome;
	private String email;
	private String corsoDiLaurea;
	private String voto;
	private String statoDellaValutazione;
		
	public void setMatricola(int matricola)
	{
		this.matricola=matricola;
	}
	
	public void setIdCorso(int idCorso)
	{
		this.idCorso=idCorso;
	}
	
	public void setData (Date data)	{
		this.data=data;
	}
	
	public void setCognome(String cognome)
	{
		this.cognome=cognome;
	}
	
	public void setNome(String nome)
	{
		this.nome=nome;
	}
	
	public void setEmail(String email)
	{
		this.email=email;
	}
	
	public void setCorsoDiLaurea (String corsoDiLaurea)
	{
		this.corsoDiLaurea=corsoDiLaurea;
	}
	
	public void setVoto(String voto)
	{
		this.voto=voto;
	}
	
	public void setStatoDellaValutazione (String statoDellaValutazione)
	{
		this.statoDellaValutazione=statoDellaValutazione;
	}
	
	
	public int getMatricola() {
		return matricola;
	}
	
	public int getIdCorso() {
		return idCorso;
	}
	
	
	public Date getData() {
		return data;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getCorsoDiLaurea() {
		return corsoDiLaurea;
	}
	
	public String getVoto() {
		return voto;
	}
	
	public String getStatoDellaValutazione() {
		return statoDellaValutazione;
	}
} 