package it.project.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.random.RandomGeneratorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.project.model.Credentials;
import it.project.model.User;
import it.project.repository.CredentialsRepository;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;
    
   
    @Transactional
    public void deleteById(Long id) {
    	credentialsRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteByUser(User utente) {
    	credentialsRepository.deleteByUser(utente);
    }
    
    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    public Credentials getCredentialsByUsername(String username) {
        Optional<Credentials> result = credentialsRepository.findByUsername(username);
        return result.orElseThrow(() -> new NoSuchElementException("No credentials found for username: " + username));
    }

    public User getUserByCredentials(String username) {
        Credentials credentials = getCredentialsByUsername(username);
        return credentials.getUser();
    }
    
    
    public Credentials getUserByCredentialsUser(User utente) {
            Optional<Credentials> result = credentialsRepository.findByUser(utente);
            return result.orElseThrow(() -> new NoSuchElementException("No credentials found for username: " + utente.getName()));
        
    }

    public void saveCredentialsFromOidcUser( User oidcUser) {
        Credentials credentials = new Credentials();
        credentials.setUsername(oidcUser.getEmail());
        credentials.setPassword(this.passwordEncoder.encode(RandomGeneratorFactory.getDefault().toString())); // Puoi impostare una password di default o gestirla diversamente
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setUser(oidcUser);
        credentialsRepository.save(credentials);
    }

	public boolean existsByUsername(String email) {
		return	this.credentialsRepository.existsByUsername(email);
	}
   
	public boolean usernameExists(String username) {
        return credentialsRepository.findByUsername(username).isPresent();
    }
	
	

}
