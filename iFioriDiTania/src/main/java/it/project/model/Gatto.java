package it.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Gatto {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome_gatto;
	
	private String descrizione_gatto;
	
	private Boolean archiviato_gatto;
	
	@Column(length = 64)
	private List<String> foto_gatto = new ArrayList<>();

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
	 * @return the nome_gatto
	 */
	public String getNome_gatto() {
		return nome_gatto;
	}

	/**
	 * @param nome_gatto the nome_gatto to set
	 */
	public void setNome_gatto(String nome_gatto) {
		this.nome_gatto = nome_gatto;
	}

	/**
	 * @return the descrizione_gatto
	 */
	public String getDescrizione_gatto() {
		return descrizione_gatto;
	}

	/**
	 * @param descrizione_gatto the descrizione_gatto to set
	 */
	public void setDescrizione_gatto(String descrizione_gatto) {
		this.descrizione_gatto = descrizione_gatto;
	}

	/**
	 * @return the archiviato_gatto
	 */
	public Boolean getArchiviato_gatto() {
		return archiviato_gatto;
	}

	/**
	 * @param archiviato_gatto the archiviato_gatto to set
	 */
	public void setArchiviato_gatto(Boolean archiviato_gatto) {
		this.archiviato_gatto = archiviato_gatto;
	}

	/**
	 * @return the foto_gatto
	 */
	public List<String> getFoto_gatto() {
		return foto_gatto;
	}

	/**
	 * @param foto_gatto the foto_gatto to set
	 */
	public void setFoto_gatto(List<String> foto_gatto) {
		this.foto_gatto = foto_gatto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gatto other = (Gatto) obj;
		return Objects.equals(id, other.id);
	}



	

	
}
