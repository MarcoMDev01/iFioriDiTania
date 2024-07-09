package it.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.project.model.Fiore;
import it.project.model.Mazzo;
import it.project.repository.MazzoRepository;
import jakarta.transaction.Transactional;


@Service
public class MazzoService {

	@Autowired
	MazzoRepository mazzoRepository;

	@Transactional
	public void deleteById(Long id) {
	    mazzoRepository.deleteById(id);
	}

	
	public Mazzo getMazzoById(Long mazzoId) {
	    Mazzo mazzo = this.mazzoRepository.findById(mazzoId).orElse(null);
	    if (mazzo != null) {
	        return mazzo;
	    } else {
	        return null;
	    }
	}

	public boolean saveMazzo(Mazzo mazzo) {
	    if (mazzo != null) {
	        mazzoRepository.save(mazzo);
	        return true;
	    } else {
	        return false;
	    }
	}

	public Iterable<Mazzo> getAllMazzi() {
	    return mazzoRepository.findAll();
	}
	
    @Transactional
    public void deleteMazzo(Long id) {

            mazzoRepository.deleteById(id);
        
    }

}
