package it.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.project.model.Gatto;
import it.project.repository.GattoRepository;
import jakarta.transaction.Transactional;

@Service
public class GattoService {

	@Autowired
	GattoRepository gattoRepository;
	
	
    @Transactional
    public void deleteById(Long id) {
    	gattoRepository.deleteById(id);
    }
	
	public Gatto getGattoById(Long gattoId) {
		Gatto gatto = this.gattoRepository.findById(gattoId).orElse(null);
		if(gatto!=null) {
			return gatto;
		}
		else {
		return null;
		}
	}
	
	   public Optional<Gatto> findById(Long id) {
	        return gattoRepository.findById(id);
	    }
	
    public Gatto saveGatto(Gatto gatto) {
        return gattoRepository.save(gatto);
    }

    
    public Iterable<Gatto> getAllGatti() {
        return gattoRepository.findAll();
    }
    

}
