package it.project.service;

import it.project.model.Evento;
import it.project.model.Fiore;
import it.project.model.Mazzo;
import it.project.model.Servizio;
import it.project.model.Accessorio;
import it.project.model.Recensione;
import it.project.repository.EventoRepository;
import it.project.repository.FioreRepository;
import it.project.repository.MazzoRepository;
import it.project.repository.ServizioRepository;
import jakarta.transaction.Transactional;
import it.project.repository.AccessorioRepository;
import it.project.repository.RecensioneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private FioreRepository fioreRepository;

    @Autowired
    private MazzoRepository mazzoRepository;

    @Autowired
    private ServizioRepository servizioRepository;

    @Autowired
    private AccessorioRepository accessorioRepository;

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Transactional
    public void deleteById(Long id) {
        eventoRepository.deleteById(id);
    }
    
    public Evento getEventoById(Long eventoId) {
        Evento evento = this.eventoRepository.findById(eventoId).orElse(null);
        if(evento != null) {
            return evento;
        } else {
            return null;
        }
    }
    
    public Optional<Evento> findById(Long id) {
        return eventoRepository.findById(id);
    }
    
    public Evento saveEvento(Evento evento) {
        return eventoRepository.save(evento);
    }
    
    public Iterable<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

    public Optional<Fiore> getFioreById(Long id) {
        return fioreRepository.findById(id);
    }

    public Optional<Mazzo> getMazzoById(Long id) {
        return mazzoRepository.findById(id);
    }

    public Optional<Servizio> getServizioById(Long id) {
        return servizioRepository.findById(id);
    }

    public Optional<Accessorio> getAccessorioById(Long id) {
        return accessorioRepository.findById(id);
    }

    public Optional<Recensione> getRecensioneById(Long id) {
        return recensioneRepository.findById(id);
    }
}
