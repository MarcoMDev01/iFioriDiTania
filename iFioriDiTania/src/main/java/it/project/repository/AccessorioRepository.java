package it.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.project.model.Accessorio;

public interface AccessorioRepository extends CrudRepository<Accessorio, Long>{
	@Query("SELECT DISTINCT f.categoria_accessorio FROM Accessorio f WHERE f.categoria_accessorio IS NOT NULL")
    List<String> findDistinctCategories();
}
