package it.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Servizio {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome_servizio;
	
	private String categoria_servizio;
	
	private String descrizione_servizio;
	
	private Boolean archiviato_servizio;

	
	@Column(length = 64)
	private List<String> foto_servizio = new ArrayList<>();

    @ManyToMany(mappedBy = "servizi_evento")
    private List<Evento> eventi = new ArrayList<>();
    
    

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
	 * @return the nome_servizio
	 */
	public String getNome_servizio() {
		return nome_servizio;
	}


	/**
	 * @param nome_servizio the nome_servizio to set
	 */
	public void setNome_servizio(String nome_servizio) {
		this.nome_servizio = nome_servizio;
	}


	/**
	 * @return the categoria_servizio
	 */
	public String getCategoria_servizio() {
		return categoria_servizio;
	}


	/**
	 * @param categoria_servizio the categoria_servizio to set
	 */
	public void setCategoria_servizio(String categoria_servizio) {
		this.categoria_servizio = categoria_servizio;
	}


	/**
	 * @return the descrizione_servizio
	 */
	public String getDescrizione_servizio() {
		return descrizione_servizio;
	}


	/**
	 * @param descrizione_servizio the descrizione_servizio to set
	 */
	public void setDescrizione_servizio(String descrizione_servizio) {
		this.descrizione_servizio = descrizione_servizio;
	}


	/**
	 * @return the archiviato_servizio
	 */
	public Boolean getArchiviato_servizio() {
		return archiviato_servizio;
	}


	/**
	 * @param archiviato_servizio the archiviato_servizio to set
	 */
	public void setArchiviato_servizio(Boolean archiviato_servizio) {
		this.archiviato_servizio = archiviato_servizio;
	}


	/**
	 * @return the foto_servizio
	 */
	public List<String> getFoto_servizio() {
		return foto_servizio;
	}


	/**
	 * @param foto_servizio the foto_servizio to set
	 */
	public void setFoto_servizio(List<String> foto_servizio) {
		this.foto_servizio = foto_servizio;
	}


	@Override
	public int hashCode() {
		return Objects.hash(archiviato_servizio, categoria_servizio, descrizione_servizio, id, nome_servizio);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servizio other = (Servizio) obj;
		return Objects.equals(archiviato_servizio, other.archiviato_servizio)
				&& Objects.equals(categoria_servizio, other.categoria_servizio)
				&& Objects.equals(descrizione_servizio, other.descrizione_servizio) && Objects.equals(id, other.id)
				&& Objects.equals(nome_servizio, other.nome_servizio);
	}



}
