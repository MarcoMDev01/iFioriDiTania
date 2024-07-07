package it.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Evento {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    @Column(name = "data_evento")
    private LocalDate dataEvento;
	
	private String nome_evento;
	
	private String categoria_evento;
	
	private String descrizione_evento;
	
	private Boolean archiviato_evento;

	
	@Column(length = 64)
	private List<String> foto_evento = new ArrayList<>();
    
	@ManyToMany
	private List<Fiore> fiori_evento= new ArrayList<>();
	
	@ManyToMany
	private List<Mazzo> mazzi_evento= new ArrayList<>();
	
	@ManyToMany
	private List<Servizio> servizi_evento= new ArrayList<>();
	
	@ManyToMany
	private List<Accessorio> accessori_evento= new ArrayList<>();
	
	@OneToMany
	private List<Recensione> recensioni_evento= new ArrayList<>();

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
	 * @return the dataEvento
	 */
	public LocalDate getDataEvento() {
		return dataEvento;
	}

	/**
	 * @param dataEvento the dataEvento to set
	 */
	public void setDataEvento(LocalDate dataEvento) {
		this.dataEvento = dataEvento;
	}

	/**
	 * @return the nome_evento
	 */
	public String getNome_evento() {
		return nome_evento;
	}

	/**
	 * @param nome_evento the nome_evento to set
	 */
	public void setNome_evento(String nome_evento) {
		this.nome_evento = nome_evento;
	}

	/**
	 * @return the categoria_evento
	 */
	public String getCategoria_evento() {
		return categoria_evento;
	}

	/**
	 * @param categoria_evento the categoria_evento to set
	 */
	public void setCategoria_evento(String categoria_evento) {
		this.categoria_evento = categoria_evento;
	}

	/**
	 * @return the descrizione_evento
	 */
	public String getDescrizione_evento() {
		return descrizione_evento;
	}

	/**
	 * @param descrizione_evento the descrizione_evento to set
	 */
	public void setDescrizione_evento(String descrizione_evento) {
		this.descrizione_evento = descrizione_evento;
	}

	/**
	 * @return the archiviato_evento
	 */
	public Boolean getArchiviato_evento() {
		return archiviato_evento;
	}

	/**
	 * @param archiviato_evento the archiviato_evento to set
	 */
	public void setArchiviato_evento(Boolean archiviato_evento) {
		this.archiviato_evento = archiviato_evento;
	}

	/**
	 * @return the foto_evento
	 */
	public List<String> getFoto_evento() {
		return foto_evento;
	}

	/**
	 * @param foto_evento the foto_evento to set
	 */
	public void setFoto_evento(List<String> foto_evento) {
		this.foto_evento = foto_evento;
	}

	/**
	 * @return the fiori_evento
	 */
	public List<Fiore> getFiori_evento() {
		return fiori_evento;
	}

	/**
	 * @param fiori_evento the fiori_evento to set
	 */
	public void setFiori_evento(List<Fiore> fiori_evento) {
		this.fiori_evento = fiori_evento;
	}

	/**
	 * @return the mazzi_evento
	 */
	public List<Mazzo> getMazzi_evento() {
		return mazzi_evento;
	}

	/**
	 * @param mazzi_evento the mazzi_evento to set
	 */
	public void setMazzi_evento(List<Mazzo> mazzi_evento) {
		this.mazzi_evento = mazzi_evento;
	}

	/**
	 * @return the servizi_evento
	 */
	public List<Servizio> getServizi_evento() {
		return servizi_evento;
	}

	/**
	 * @param servizi_evento the servizi_evento to set
	 */
	public void setServizi_evento(List<Servizio> servizi_evento) {
		this.servizi_evento = servizi_evento;
	}

	/**
	 * @return the accessori_evento
	 */
	public List<Accessorio> getAccessori_evento() {
		return accessori_evento;
	}

	/**
	 * @param accessori_evento the accessori_evento to set
	 */
	public void setAccessori_evento(List<Accessorio> accessori_evento) {
		this.accessori_evento = accessori_evento;
	}

	/**
	 * @return the recensioni_evento
	 */
	public List<Recensione> getRecensioni_evento() {
		return recensioni_evento;
	}

	/**
	 * @param recensioni_evento the recensioni_evento to set
	 */
	public void setRecensioni_evento(List<Recensione> recensioni_evento) {
		this.recensioni_evento = recensioni_evento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessori_evento, categoria_evento, dataEvento, fiori_evento, id, mazzi_evento, nome_evento,
				servizi_evento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		return Objects.equals(accessori_evento, other.accessori_evento)
				&& Objects.equals(categoria_evento, other.categoria_evento)
				&& Objects.equals(dataEvento, other.dataEvento) && Objects.equals(fiori_evento, other.fiori_evento)
				&& Objects.equals(id, other.id) && Objects.equals(mazzi_evento, other.mazzi_evento)
				&& Objects.equals(nome_evento, other.nome_evento)
				&& Objects.equals(servizi_evento, other.servizi_evento);
	}

	

}
