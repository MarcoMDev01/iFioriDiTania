package it.project.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.project.model.Fiore;

public interface FioreRepository extends CrudRepository<Fiore, Long>{
	@Query("SELECT DISTINCT f.categoria_fiore FROM Fiore f WHERE f.categoria_fiore IS NOT NULL")
    List<String> findDistinctCategories();
    @Query("SELECT f FROM Fiore f WHERE f.categoria_fiore LIKE '%piante%'")
    List<Fiore> findFioriByCategoriaContainingPiante();
}
