package it.project.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.project.model.Recensione;



public interface RecensioneRepository extends CrudRepository<Recensione, Long>{
	 List<Recensione> findByEventoRecensitoIsNullAndApprovazioneIsTrue();
}
