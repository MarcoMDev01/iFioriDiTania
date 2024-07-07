package it.project.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.project.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);

    boolean existsByEmailAndProvider(String email, String provider);

	User findByEmailAndProvider(String email, String provider);
    
   
}