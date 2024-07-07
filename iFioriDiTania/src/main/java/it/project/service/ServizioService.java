package it.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.project.model.Servizio;
import it.project.repository.ServizioRepository;
import jakarta.transaction.Transactional;

@Service
public class ServizioService {

    @Autowired
    ServizioRepository servizioRepository;
    
    @Transactional
    public void deleteById(Long id) {
        servizioRepository.deleteById(id);
    }
    
    public Servizio getServizioById(Long servizioId) {
        Servizio servizio = this.servizioRepository.findById(servizioId).orElse(null);
        if (servizio != null) {
            return servizio;
        } else {
            return null;
        }
    }
    
    public Optional<Servizio> findById(Long id) {
        return servizioRepository.findById(id);
    }
    
    public Servizio saveServizio(Servizio servizio) {
        return servizioRepository.save(servizio);
    }

    public Iterable<Servizio> getAllServizi() {
        return servizioRepository.findAll();
    }
}
