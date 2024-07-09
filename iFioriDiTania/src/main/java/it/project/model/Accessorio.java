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
public class Accessorio {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome_accessorio;
	
	private String categoria_accessorio;
	
	private String descrizione_accessorio;
	
	private Boolean archiviato_accessorio;
	
	@Column(length = 64)
	private List<String> foto_accessorio = new ArrayList<>();

    @ManyToMany(mappedBy = "accessoriDelMazzo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Mazzo> mazziDelAccessorio= new ArrayList<>();
    
    /**
	 * @return the mazziDelAccessorio
	 */
	public List<Mazzo> getMazziDelAccessorio() {
		return mazziDelAccessorio;
	}


	/**
	 * @param mazziDelAccessorio the mazziDelAccessorio to set
	 */
	public void setMazziDelAccessorio(List<Mazzo> mazziDelAccessorio) {
		this.mazziDelAccessorio = mazziDelAccessorio;
	}


	@ManyToMany(mappedBy = "accessori_evento")
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
	 * @return the nome_accessorio
	 */
	public String getNome_accessorio() {
		return nome_accessorio;
	}


	/**
	 * @param nome_accessorio the nome_accessorio to set
	 */
	public void setNome_accessorio(String nome_accessorio) {
		this.nome_accessorio = nome_accessorio;
	}


	/**
	 * @return the categoria_accessorio
	 */
	public String getCategoria_accessorio() {
		return categoria_accessorio;
	}


	/**
	 * @param categoria_accessorio the categoria_accessorio to set
	 */
	public void setCategoria_accessorio(String categoria_accessorio) {
		this.categoria_accessorio = categoria_accessorio;
	}


	/**
	 * @return the descrizione_accessorio
	 */
	public String getDescrizione_accessorio() {
		return descrizione_accessorio;
	}


	/**
	 * @param descrizione_accessorio the descrizione_accessorio to set
	 */
	public void setDescrizione_accessorio(String descrizione_accessorio) {
		this.descrizione_accessorio = descrizione_accessorio;
	}


	/**
	 * @return the archiviato_accessorio
	 */
	public Boolean getArchiviato_accessorio() {
		return archiviato_accessorio;
	}


	/**
	 * @param archiviato_accessorio the archiviato_accessorio to set
	 */
	public void setArchiviato_accessorio(Boolean archiviato_accessorio) {
		this.archiviato_accessorio = archiviato_accessorio;
	}


	/**
	 * @return the foto_accessorio
	 */
	public List<String> getFoto_accessorio() {
		return foto_accessorio;
	}


	/**
	 * @param foto_accessorio the foto_accessorio to set
	 */
	public void setFoto_accessorio(List<String> foto_accessorio) {
		this.foto_accessorio = foto_accessorio;
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


	@Override
	public int hashCode() {
		return Objects.hash(archiviato_accessorio, categoria_accessorio, descrizione_accessorio, id, nome_accessorio);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Accessorio other = (Accessorio) obj;
		return Objects.equals(archiviato_accessorio, other.archiviato_accessorio)
				&& Objects.equals(categoria_accessorio, other.categoria_accessorio)
				&& Objects.equals(descrizione_accessorio, other.descrizione_accessorio) && Objects.equals(id, other.id)
				&& Objects.equals(nome_accessorio, other.nome_accessorio);
	}
	
	
	
	
	
}
