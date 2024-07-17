package it.project.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.project.model.Mazzo;

public interface MazzoRepository extends CrudRepository<Mazzo, Long>{
	@Query("SELECT DISTINCT m.categoria_mazzo FROM Mazzo m WHERE m.categoria_mazzo IS NOT NULL")
    List<String> findDistinctCategories();
}
