package it.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.project.model.Accessorio;
import it.project.model.Mazzo;
import it.project.repository.AccessorioRepository;
import jakarta.transaction.Transactional;

@Service
public class AccessorioService {

    @Autowired
    AccessorioRepository accessorioRepository;
    
	@Autowired
	MazzoService mazzoService;
	
    @Transactional
    public void deleteById(Long id) {
        accessorioRepository.deleteById(id);
    }
    
    public Accessorio getAccessorioById(Long accessorioId) {
        Accessorio accessorio = this.accessorioRepository.findById(accessorioId).orElse(null);
        if(accessorio != null) {
            return accessorio;
        } else {
            return null;
        }
    }
    
    public Optional<Accessorio> findById(Long id) {
        return accessorioRepository.findById(id);
    }
    
    public Accessorio saveAccessorio(Accessorio accessorio) {
        return accessorioRepository.save(accessorio);
    }

    public Iterable<Accessorio> getAllAccessori() {
        return accessorioRepository.findAll();
    }
    
    @Transactional
    public void deleteAccessorio(Long id) {
        Optional<Accessorio> optionalAccessorio = accessorioRepository.findById(id);
        if (optionalAccessorio.isPresent()) {
        	Accessorio accessorio = optionalAccessorio.get();
            for (Mazzo mazzo : accessorio.getMazziDelAccessorio()) {
                mazzo.getAccessoriDelMazzo().remove(accessorio);
                mazzoService.saveMazzo(mazzo);
            }
            accessorioRepository.delete(accessorio);
        }
    }
    
    public List<String> getDistinctCategories() {
        return accessorioRepository.findDistinctCategories();
    }
}
