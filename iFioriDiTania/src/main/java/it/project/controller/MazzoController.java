package it.project.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.project.model.Accessorio;
import it.project.model.Fiore;
import it.project.model.Mazzo;
import it.project.model.User;
import it.project.service.AccessorioService;
import it.project.service.FioreService;
import it.project.service.MazzoService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per la gestione delle operazioni sui mazzi.
 * Gestisce la visualizzazione, aggiunta, modifica e rimozione dei mazzi.
 */
@Controller
public class MazzoController {

    @Autowired
    private MazzoService mazzoService;
    
    @Autowired
    private FioreService fioreService ;
    

    @Autowired
    private AccessorioService accessorioService;
    
    /**
     * Metodo per visualizzare la pagina di un singolo mazzo.
     * 
     * @param mazzoId ID del mazzo da visualizzare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la visualizzazione del mazzo.
     */
    @GetMapping("/mazzo/{id}")
    private String PaginaMazzo(@PathVariable("id") Long mazzoId, @ModelAttribute("utente") User utente, Model model) {       
        model.addAttribute("mazzo", this.mazzoService.getMazzoById(mazzoId));
        model.addAttribute("utente", utente);
        return "mazzo.html";
    }
    
    /**
     * Metodo per visualizzare la pagina di modifica di un mazzo (solo per amministratori).
     * 
     * @param mazzoId ID del mazzo da modificare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la modifica del mazzo.
     */
    @GetMapping("/admin/PaginaModificaMazzo/{id}")
    private String PaginaModificaMazzo(@PathVariable("id") Long mazzoId, @ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("mazzo", this.mazzoService.getMazzoById(mazzoId));
   	 	List<Accessorio> TuttiAccessori= (List<Accessorio>) accessorioService.getAllAccessori();
   	 	TuttiAccessori.removeAll(mazzoService.getMazzoById(mazzoId).getAccessoriDelMazzo());
        model.addAttribute("tutti_accessori", TuttiAccessori);
   	 	List<Fiore> TuttiFiori= (List<Fiore>) fioreService.getAllFiori();
   	 	TuttiFiori.removeAll(mazzoService.getMazzoById(mazzoId).getFioriDelMazzo());
        model.addAttribute("tutti_fiori", TuttiFiori);

        return "admin/pagine_mazzo_amministratore/PaginaModificaMazzo.html";
    }

    /**
     * Metodo per aggiornare le informazioni di un mazzo esistente.
     * 
     * @param mazzo  Oggetto Mazzo con le informazioni aggiornate.
     * @param result Oggetto BindingResult per la gestione degli errori di validazione.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del mazzo.
     */
    @PostMapping("/admin/PaginaModificaMazzo/modifica")
    private String modificaMazzo(@ModelAttribute("mazzo") Mazzo mazzo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mazzo", mazzo);
            return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
        }

        // Carica il mazzo originale dal database
        Mazzo mazzoOriginale = mazzoService.getMazzoById(mazzo.getId());
        if (mazzoOriginale != null) {
            // Mantieni le foto esistenti
            mazzo.setFoto_mazzo(mazzoOriginale.getFoto_mazzo());
            mazzoService.saveMazzo(mazzo);
        }

        return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
    }

    /**
     * Metodo per visualizzare la pagina di aggiunta di un nuovo mazzo (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per l'aggiunta di un mazzo.
     */
    @GetMapping("/admin/pagina_aggiungiMazzo")
    private String PaginaAggiungiMazzo(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("mazzo", new Mazzo());
        return "admin/pagine_mazzo_amministratore/PaginaAggiungiMazzo.html";
    }
    
    /**
     * Metodo per aggiungere un nuovo mazzo al sistema.
     * 
     * @param multipartFile Il file dell'immagine del mazzo.
     * @param mazzo         Oggetto Mazzo con le informazioni del nuovo mazzo.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del mazzo appena aggiunto.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/aggiungiMazzo")
    private String AggiungiMazzo(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("mazzo") Mazzo mazzo, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            mazzo.getFoto_mazzo().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_mazzi";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        mazzoService.saveMazzo(mazzo);
        model.addAttribute("utente", utente);
        model.addAttribute("mazzo", mazzo);
        return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
    }
    
    /**
     * Metodo per visualizzare la pagina con la lista di tutti i mazzi.
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i mazzi.
     */
    @GetMapping("/mazzi")
    private String getAllMazzi(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_mazzi", mazzoService.getAllMazzi());
        model.addAttribute("utente", utente);
        return "mazzi.html";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti i mazzi (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i mazzi.
     */
    @GetMapping("/admin/mazzi")
    private String AdminGetAllMazzi(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_mazzi", mazzoService.getAllMazzi());
        model.addAttribute("utente", utente);
        return "admin/pagine_mazzo_amministratore/mazzi.html";
    }
    
    /**
     * Metodo per rimuovere un mazzo dal sistema.
     * 
     * @param mazzoId ID del mazzo da rimuovere.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina con la lista di tutti i mazzi.
     * @throws IOException In caso di errori durante l'eliminazione dei file.
     */
    @PostMapping("/admin/rimuoviMazzo/{id}")
    private String removeMazzo(@PathVariable("id") Long mazzoId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Mazzo mazzo = mazzoService.getMazzoById(mazzoId);
        List<String> fotoMazzo = mazzo.getFoto_mazzo();
        String uploadDir = "src/main/resources/static/images/foto_mazzi";
        for (String foto : fotoMazzo) {
            FileUploadUtil.deleteFile(uploadDir, foto);
        }
        
        mazzoService.deleteById(mazzoId);
        model.addAttribute("utente", utente);
        return "redirect:/admin/mazzi";
    }
    

    ///////////////////////////foto/////////////////////////////
    
    /**
     * Metodo per salvare una foto per un mazzo esistente.
     * 
     * @param multipartFile Il file dell'immagine del mazzo.
     * @param mazzoId       ID del mazzo a cui associare l'immagine.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del mazzo.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/MazzosavePhoto/{mazzoId}")
    private String saveMazzoPhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable("mazzoId") Long mazzoId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Mazzo mazzo = mazzoService.getMazzoById(mazzoId);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        mazzo.getFoto_mazzo().add(fileName);
        String uploadDir = "src/main/resources/static/images/foto_mazzi";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        mazzoService.saveMazzo(mazzo);
        model.addAttribute("utente", utente);
        model.addAttribute("mazzo", mazzo);
        return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
    }
    
    /**
     * Metodo per rimuovere una foto da un mazzo esistente.
     * 
     * @param imageName Il nome dell'immagine da rimuovere.
     * @param mazzoId   ID del mazzo da cui rimuovere l'immagine.
     * @param model     Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del mazzo.
     * @throws IOException In caso di errori durante l'eliminazione del file.
     */
    @PostMapping("/admin/mazzo/removePhoto/{mazzoId}")
    private String removeMazzoPhoto(@RequestParam("image") String imageName, @PathVariable("mazzoId") Long mazzoId, Model model) throws IOException {
        Mazzo mazzo = mazzoService.getMazzoById(mazzoId);
        if (mazzo != null) {
            mazzo.getFoto_mazzo().remove(imageName);
            String uploadDir = "src/main/resources/static/images/foto_mazzi";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            mazzoService.saveMazzo(mazzo); // Assicurati di salvare le modifiche al mazzo
            return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
        }
        return "redirect:/";
    }


    
    ///////////////////////////////// gestione dati ////////////////////////////////
    
    /**
     * Metodo per aggiungere un fiore al mazzo al sistema.
     * 
     * @param mazzo         Oggetto Mazzo con le informazioni del nuovo mazzo.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del mazzo appena aggiunto.
     */
    @PostMapping("/admin/aggiungiFioreAlMazzo/{fioreId}")
    private String AggiungiFiorialMazzo( @RequestParam Long mazzoId,@PathVariable("fioreId") Long fioreId, @ModelAttribute("utente") User utente, Model model)  {
        Mazzo mazzo = mazzoService.getMazzoById(mazzoId);         
        mazzo.getFioriDelMazzo().add(fioreService.getFioreById(fioreId));
        mazzoService.saveMazzo(mazzo);
        model.addAttribute("utente", utente);
        model.addAttribute("mazzo", mazzo);
        return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
    }
    /**
     * Metodo per aggiungere un fiore al mazzo al sistema.
     * 
     * @param mazzo         Oggetto Mazzo con le informazioni del nuovo mazzo.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del mazzo appena aggiunto.
     */
    @PostMapping("/admin/aggiungiAccessorioAlMazzo/{accessorioId}")
    private String AggiungiAccessorialMazzo( @RequestParam Long mazzoId,@PathVariable("accessorioId") Long accessorioId, @ModelAttribute("utente") User utente, Model model)  {
        Mazzo mazzo = mazzoService.getMazzoById(mazzoId);         
        mazzo.getAccessoriDelMazzo().add(accessorioService.getAccessorioById(accessorioId));
        mazzoService.saveMazzo(mazzo);
        model.addAttribute("utente", utente);
        model.addAttribute("mazzo", mazzo);
        return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
    }
    
    
    
    @PostMapping("/admin/mazzo/fiore/rimuovi/{fioreId}")
    private String rimuoviFioreDalMazzo(Model model,@PathVariable("fioreId") Long fioreId, @RequestParam ("mazzoId") Long mazzoId){
    	Mazzo mazzo= mazzoService.getMazzoById(mazzoId);
    	Fiore fiore = fioreService.getFioreById(fioreId);

    	mazzo.getFioriDelMazzo().remove(fiore);
    	mazzoService.saveMazzo(mazzo);
        return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
    	}
    
    @PostMapping("/admin/mazzo/accessorio/rimuovi/{accessorioId}")
    private String rimuoviAccessorioDalMazzo(Model model, @PathVariable("accessorioId") Long accessorioId, @RequestParam("mazzoId") Long mazzoId) {
        Mazzo mazzo = mazzoService.getMazzoById(mazzoId);
        Accessorio accessorio = accessorioService.getAccessorioById(accessorioId);

        mazzo.getAccessoriDelMazzo().remove(accessorio);
        mazzoService.saveMazzo(mazzo);
        return "redirect:/admin/PaginaModificaMazzo/" + mazzo.getId();
    }
}
