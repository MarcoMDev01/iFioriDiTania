package it.project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.project.model.Credentials;
import it.project.model.User;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);
	public Optional<Credentials> findByUser(User user);
	

	
	public boolean existsByUsername(String username);
	public void deleteByUser(User utente);
	
}