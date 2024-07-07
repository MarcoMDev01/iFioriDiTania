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
public class Fiore {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome_fiore;
	
	private String categoria_fiore;
	
	private String descrizione_fiore;

	private Boolean archiviato_fiore;

    @ManyToMany(mappedBy = "fioriDelMazzo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Mazzo> mazziDelFiore = new ArrayList<>();
    
    @ManyToMany(mappedBy = "fiori_evento")
    private List<Evento> eventi = new ArrayList<>();
	
	@Column(length = 64)
	private List<String> foto_fiore = new ArrayList<>();

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
	 * @return the nome_fiore
	 */
	public String getNome_fiore() {
		return nome_fiore;
	}

	/**
	 * @param nome_fiore the nome_fiore to set
	 */
	public void setNome_fiore(String nome_fiore) {
		this.nome_fiore = nome_fiore;
	}

	/**
	 * @return the categoria_fiore
	 */
	public String getCategoria_fiore() {
		return categoria_fiore;
	}

	/**
	 * @param categoria_fiore the categoria_fiore to set
	 */
	public void setCategoria_fiore(String categoria_fiore) {
		this.categoria_fiore = categoria_fiore;
	}

	/**
	 * @return the descrizione_fiore
	 */
	public String getDescrizione_fiore() {
		return descrizione_fiore;
	}

	/**
	 * @param descrizione_fiore the descrizione_fiore to set
	 */
	public void setDescrizione_fiore(String descrizione_fiore) {
		this.descrizione_fiore = descrizione_fiore;
	}

	/**
	 * @return the archiviato_fiore
	 */
	public Boolean getArchiviato_fiore() {
		return archiviato_fiore;
	}

	/**
	 * @param archiviato_fiore the archiviato_fiore to set
	 */
	public void setArchiviato_fiore(Boolean archiviato_fiore) {
		this.archiviato_fiore = archiviato_fiore;
	}

	/**
	 * @return the mazziDelFiore
	 */
	public List<Mazzo> getMazziDelFiore() {
		return mazziDelFiore;
	}

	/**
	 * @param mazziDelFiore the mazziDelFiore to set
	 */
	public void setMazziDelFiore(List<Mazzo> mazziDelFiore) {
		this.mazziDelFiore = mazziDelFiore;
	}

	/**
	 * @return the foto_fiore
	 */
	public List<String> getFoto_fiore() {
		return foto_fiore;
	}

	/**
	 * @param foto_fiore the foto_fiore to set
	 */
	public void setFoto_fiore(List<String> foto_fiore) {
		this.foto_fiore = foto_fiore;
	}

	@Override
	public int hashCode() {
		return Objects.hash(archiviato_fiore, categoria_fiore, descrizione_fiore, foto_fiore, id, mazziDelFiore,
				nome_fiore);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fiore other = (Fiore) obj;
		return Objects.equals(archiviato_fiore, other.archiviato_fiore)
				&& Objects.equals(categoria_fiore, other.categoria_fiore)
				&& Objects.equals(descrizione_fiore, other.descrizione_fiore)
				&& Objects.equals(foto_fiore, other.foto_fiore) && Objects.equals(id, other.id)
				&& Objects.equals(mazziDelFiore, other.mazziDelFiore) && Objects.equals(nome_fiore, other.nome_fiore);
	}

	
	
}
