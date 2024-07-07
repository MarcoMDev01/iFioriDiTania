package it.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.project.model.Recensione;
import it.project.repository.RecensioneRepository;
import jakarta.transaction.Transactional;

@Service
public class RecensioneService {

    @Autowired
    RecensioneRepository recensioneRepository;
    
    @Transactional
    public void deleteById(Long id) {
        recensioneRepository.deleteById(id);
    }
    
    public Recensione getRecensioneById(Long recensioneId) {
        Recensione recensione = this.recensioneRepository.findById(recensioneId).orElse(null);
        if(recensione != null) {
            return recensione;
        }
        else {
            return null;
        }
    }
    
    public Optional<Recensione> findById(Long id) {
        return recensioneRepository.findById(id);
    }
    
    public Recensione saveRecensione(Recensione recensione) {
        return recensioneRepository.save(recensione);
    }

    public Iterable<Recensione> getAllRecensioni() {
        return recensioneRepository.findAll();
    }

}
