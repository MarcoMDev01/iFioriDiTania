package it.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Recensione {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String descrizione_recensione;

	@Column(length = 64)
	private List<String> foto_recensione = new ArrayList<>();

	@ManyToOne
	private Evento evento_Recensito;
	
	@ManyToOne
	private User utente_Recensione ;
	
	private String approvazione;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the descrizione_recensione
	 */
	public String getDescrizione_recensione() {
		return descrizione_recensione;
	}

	/**
	 * @param descrizione_recensione the descrizione_recensione to set
	 */
	public void setDescrizione_recensione(String descrizione_recensione) {
		this.descrizione_recensione = descrizione_recensione;
	}

	/**
	 * @return the foto_recensione
	 */
	public List<String> getFoto_recensione() {
		return foto_recensione;
	}

	/**
	 * @param foto_recensione the foto_recensione to set
	 */
	public void setFoto_recensione(List<String> foto_recensione) {
		this.foto_recensione = foto_recensione;
	}

	/**
	 * @return the evento_Recensito
	 */
	public Evento getEvento_Recensito() {
		return evento_Recensito;
	}

	/**
	 * @param evento_Recensito the evento_Recensito to set
	 */
	public void setEvento_Recensito(Evento evento_Recensito) {
		this.evento_Recensito = evento_Recensito;
	}

	/**
	 * @return the utente_Recensione
	 */
	public User getUtente_Recensione() {
		return utente_Recensione;
	}

	/**
	 * @param utente_Recensione the utente_Recensione to set
	 */
	public void setUtente_Recensione(User utente_Recensione) {
		this.utente_Recensione = utente_Recensione;
	}

	/**
	 * @return the approvazione
	 */
	public String getApprovazione() {
		return approvazione;
	}

	/**
	 * @param approvazione the approvazione to set
	 */
	public void setApprovazione(String approvazione) {
		this.approvazione = approvazione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(approvazione, evento_Recensito, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recensione other = (Recensione) obj;
		return Objects.equals(approvazione, other.approvazione)
				&& Objects.equals(evento_Recensito, other.evento_Recensito) && Objects.equals(id, other.id);
	}

	
}
