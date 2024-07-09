package it.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.project.model.Mazzo;
import it.project.model.Fiore;
import it.project.repository.FioreRepository;
import jakarta.transaction.Transactional;


@Service
public class FioreService {
	
	@Autowired
	FioreRepository fioreRepository;
	
	@Autowired
	MazzoService mazzoService;
	
	
    @Transactional
    public void deleteById(Long id) {
    	fioreRepository.deleteById(id);
    }
	
	public Fiore getFioreById(Long fioreId) {
		Fiore fiore = this.fioreRepository.findById(fioreId).orElse(null);
		if(fiore!=null) {
			return fiore;
		}
		else {
		return null;
		}
	}
	
	public Optional<Fiore> findById(Long id) {
	        return fioreRepository.findById(id);
	    }
	
    public Fiore saveFiore(Fiore fiore) {
        return fioreRepository.save(fiore);
    }

    
    public Iterable<Fiore> getAllFiori() {
        return fioreRepository.findAll();
    }
    

    @Transactional
    public void deleteFiore(Long id) {
        Optional<Fiore> optionalFiore = fioreRepository.findById(id);
        if (optionalFiore.isPresent()) {
            Fiore fiore = optionalFiore.get();
            for (Mazzo mazzo : fiore.getMazziDelFiore()) {
                mazzo.getFioriDelMazzo().remove(fiore);
                mazzoService.saveMazzo(mazzo);
            }
            fioreRepository.delete(fiore);
        }
    }

}
