package it.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class Mazzo {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nome_mazzo;
	
	private String categoria_mazzo;
	
	private String descrizione_mazzo;
	
	private Boolean archiviato_mazzo;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Accessorio> accessoriDelMazzo = new ArrayList<>();
	
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Fiore> fioriDelMazzo = new ArrayList<>();
	
	@Column(length = 64)
	private List<String> foto_mazzo = new ArrayList<>();

    @ManyToMany(mappedBy = "mazzi_evento")
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
	 * @return the nome_mazzo
	 */
	public String getNome_mazzo() {
		return nome_mazzo;
	}

	/**
	 * @param nome_mazzo the nome_mazzo to set
	 */
	public void setNome_mazzo(String nome_mazzo) {
		this.nome_mazzo = nome_mazzo;
	}

	/**
	 * @return the categoria_mazzo
	 */
	public String getCategoria_mazzo() {
		return categoria_mazzo;
	}

	/**
	 * @param categoria_mazzo the categoria_mazzo to set
	 */
	public void setCategoria_mazzo(String categoria_mazzo) {
		this.categoria_mazzo = categoria_mazzo;
	}

	/**
	 * @return the descrizione_mazzo
	 */
	public String getDescrizione_mazzo() {
		return descrizione_mazzo;
	}

	/**
	 * @param descrizione_mazzo the descrizione_mazzo to set
	 */
	public void setDescrizione_mazzo(String descrizione_mazzo) {
		this.descrizione_mazzo = descrizione_mazzo;
	}

	/**
	 * @return the archiviato_mazzo
	 */
	public Boolean getArchiviato_mazzo() {
		return archiviato_mazzo;
	}

	/**
	 * @param archiviato_mazzo the archiviato_mazzo to set
	 */
	public void setArchiviato_mazzo(Boolean archiviato_mazzo) {
		this.archiviato_mazzo = archiviato_mazzo;
	}

	
	
	/**
	 * @return the accessoriDelMazzo
	 */
	public List<Accessorio> getAccessoriDelMazzo() {
		return accessoriDelMazzo;
	}

	/**
	 * @param accessoriDelMazzo the accessoriDelMazzo to set
	 */
	public void setAccessoriDelMazzo(List<Accessorio> accessoriDelMazzo) {
		this.accessoriDelMazzo = accessoriDelMazzo;
	}

	/**
	 * @return the eventi
	 */
	public List<Evento> getEventi() {
		return eventi;
	}

	/**
	 * @param eventi the eventi to set
	 */
	public void setEventi(List<Evento> eventi) {
		this.eventi = eventi;
	}

	/**
	 * @return the fioriDelMazzo
	 */
	public List<Fiore> getFioriDelMazzo() {
		return fioriDelMazzo;
	}

	/**
	 * @param fioriDelMazzo the fioriDelMazzo to set
	 */
	public void setFioriDelMazzo(List<Fiore> fioriDelMazzo) {
		this.fioriDelMazzo = fioriDelMazzo;
	}

	/**
	 * @return the foto_mazzo
	 */
	public List<String> getFoto_mazzo() {
		return foto_mazzo;
	}

	/**
	 * @param foto_mazzo the foto_mazzo to set
	 */
	public void setFoto_mazzo(List<String> foto_mazzo) {
		this.foto_mazzo = foto_mazzo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(archiviato_mazzo, categoria_mazzo, descrizione_mazzo, fioriDelMazzo, id, nome_mazzo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mazzo other = (Mazzo) obj;
		return Objects.equals(archiviato_mazzo, other.archiviato_mazzo)
				&& Objects.equals(categoria_mazzo, other.categoria_mazzo)
				&& Objects.equals(descrizione_mazzo, other.descrizione_mazzo)
				&& Objects.equals(fioriDelMazzo, other.fioriDelMazzo) && Objects.equals(id, other.id)
				&& Objects.equals(nome_mazzo, other.nome_mazzo);
	}

	


}