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

import it.project.model.Evento;
import it.project.model.Recensione;
import it.project.model.User;
import it.project.service.EventoService;
import it.project.service.RecensioneService;
import it.project.service.UserService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per la gestione delle operazioni sulle recensioni.
 * Gestisce la visualizzazione, aggiunta, modifica e rimozione delle recensioni.
 */
@Controller
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventoService eventoService;
    @Autowired
    private GlobalController globalController;

    
    /**
     * Metodo per visualizzare la pagina di una singola recensione.
     * 
     * @param recensioneId ID della recensione da visualizzare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la visualizzazione della recensione.
     */
    @GetMapping("/recensione/{id}")
    private String PaginaRecensione(@PathVariable("id") Long recensioneId, @ModelAttribute("utente") User utente, Model model) {       
        model.addAttribute("recensione", this.recensioneService.getRecensioneById(recensioneId));
        model.addAttribute("utente", utente);
        return "recensione.html";
    }
    
    /**
     * Metodo per visualizzare la pagina di modifica di una recensione (solo per amministratori).
     * 
     * @param recensioneId ID della recensione da modificare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la modifica della recensione.
     */
    @GetMapping("/admin/PaginaModificaRecensione/{id}")
    private String PaginaModificaRecensione(@PathVariable("id") Long recensioneId, @ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("recensione", this.recensioneService.getRecensioneById(recensioneId));
        return "admin/pagine_recensione_amministratore/PaginaModificaRecensione.html";
    }
    
    /**
     * Metodo per aggiornare le informazioni di una recensione esistente.
     * 
     * @param recensione  Oggetto Recensione con le informazioni aggiornate.
     * @param result Oggetto BindingResult per la gestione degli errori di validazione.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica della recensione.
     */
    @PostMapping("/admin/PaginaModificaRecensione/modifica")
    public String modificaRecensione(@ModelAttribute("recensione") Recensione recensione, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("recensione", recensione);
            return "redirect:/admin/PaginaModificaRecensione/" + recensione.getId();
        }

        // Carica la recensione originale dal database
        Recensione recensioneOriginale = recensioneService.getRecensioneById(recensione.getId());
        if (recensioneOriginale != null) {
            // Mantieni le foto esistenti
            recensione.setFoto_recensione(recensioneOriginale.getFoto_recensione());
            recensioneService.saveRecensione(recensione);
        }

        return "redirect:/admin/PaginaModificaRecensione/" + recensione.getId();
    }

    /**
     * Metodo per visualizzare la pagina di aggiunta di una nuova recensione (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per l'aggiunta di una recensione.
     */
    @GetMapping("/admin/pagina_aggiungiRecensione")
    private String PaginaAggiungiRecensione(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("recensione", new Recensione());
        return "admin/pagine_recensione_amministratore/PaginaAggiungiRecensione.html";
    }
    
    
    @GetMapping("/recensione/pagina_aggiungiRecensione_alSito")
    private String PaginaAggiungiRecensioneAlSito(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("recensione", new Recensione());
        return "PaginaAggiungiRecensione.html";
    }
    

    
    /**
     * Metodo per aggiungere una nuova recensione al sistema.
     * 
     * @param multipartFile Il file dell'immagine della recensione.
     * @param recensione         Oggetto Recensione con le informazioni della nuova recensione.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica della recensione appena aggiunta.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/aggiungiRecensione")
    private String AggiungiRecensione(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("recensione") Recensione recensione, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            recensione.getFoto_recensione().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_recensioni";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        recensioneService.saveRecensione(recensione);
        model.addAttribute("utente", utente);
        model.addAttribute("recensione", recensione);
        return "redirect:/admin/PaginaModificaRecensione/" + recensione.getId();
    }
    
    /**
     * Metodo per salvare una foto per una recensione esistente.
     * 
     * @param multipartFile Il file dell'immagine della recensione.
     * @param recensioneId       ID della recensione a cui associare l'immagine.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica della recensione.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/RecensionesavePhoto/{recensioneId}")
    public String saveRecensionePhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable("recensioneId") Long recensioneId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Recensione recensione = recensioneService.getRecensioneById(recensioneId);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        recensione.getFoto_recensione().add(fileName);
        String uploadDir = "src/main/resources/static/images/foto_recensioni";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        recensioneService.saveRecensione(recensione);
        model.addAttribute("utente", utente);
        model.addAttribute("recensione", recensione);
        return "redirect:/admin/PaginaModificaRecensione/" + recensione.getId();
    }
    
    /**
     * Metodo per rimuovere una foto da una recensione esistente.
     * 
     * @param imageName Il nome dell'immagine da rimuovere.
     * @param recensioneId   ID della recensione da cui rimuovere l'immagine.
     * @param model     Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica della recensione.
     * @throws IOException In caso di errori durante l'eliminazione del file.
     */
    @PostMapping("/admin/recensione/removePhoto/{recensioneId}")
    public String removeRecensionePhoto(@RequestParam("image") String imageName, @PathVariable("recensioneId") Long recensioneId, Model model) throws IOException {
        Recensione recensione = recensioneService.getRecensioneById(recensioneId);
        if (recensione != null) {
            recensione.getFoto_recensione().remove(imageName);
            String uploadDir = "src/main/resources/static/images/foto_recensioni";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            recensioneService.saveRecensione(recensione); // Assicurati di salvare le modifiche alla recensione
            return "redirect:/admin/PaginaModificaRecensione/" + recensione.getId();
        }
        return "redirect:/";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutte le recensioni.
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutte le recensioni.
     */
    @GetMapping("/recensioni")
    public String getAllRecensioni(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutte_recensioni", recensioneService.getAllRecensioni());
        model.addAttribute("utente", utente);
        return "recensioni.html";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutte le recensioni (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutte le recensioni.
     */
    @GetMapping("/admin/recensioni")
    public String AdminGetAllRecensioni(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutte_recensioni", recensioneService.getAllRecensioni());
        model.addAttribute("utente", utente);
        return "admin/pagine_recensione_amministratore/recensioni.html";
    }
    
    /**
     * Metodo per rimuovere una recensione dal sistema.
     * 
     * @param recensioneId ID della recensione da rimuovere.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina con la lista di tutte le recensioni.
     * @throws IOException In caso di errori durante l'eliminazione dei file.
     */
    @PostMapping("/admin/rimuoviRecensione/{id}")
    public String removeRecensione(@PathVariable("id") Long recensioneId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Recensione recensione = recensioneService.getRecensioneById(recensioneId);
        List<String> fotoRecensione = recensione.getFoto_recensione();
        String uploadDir = "src/main/resources/static/images/foto_recensioni";
        for (String foto : fotoRecensione) {
            FileUploadUtil.deleteFile(uploadDir, foto);
        }
        
        recensioneService.deleteById(recensioneId);
        model.addAttribute("utente", utente);
        return "redirect:/admin/recensioni";
    }
    
    
    
    @PostMapping("/admin/aggiungiRecensioneSito")
    private String AdminAggiungiRecensioneSito(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("recensione") Recensione recensione, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            recensione.getFoto_recensione().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_recensioni";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        User utente= globalController.getCurrentUser();
        if(utente!=null) {
        	recensione.setUtente_Recensione(utente);
        	recensioneService.saveRecensione(recensione);
        }else {
            recensioneService.saveRecensione(recensione);
        }
        model.addAttribute("recensione", recensione);
        return "redirect:/admin/PaginaModificaRecensione/" + recensione.getId();//TODO creare pagina recensioni sito
    }
    
    
    
    //recensioni per gli eventi 
    @PostMapping("/admin/aggiungiRecensioneEvento")
    private String AdminAggiungiRecensioneEvento(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("recensione") Recensione recensione,@RequestParam("eventoId")Long eventoId, Model model) throws IOException {
        Evento evento=eventoService.getEventoById(eventoId);
    	
    	if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            recensione.getFoto_recensione().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_recensioni";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    	
        User utente= globalController.getCurrentUser();
        if(utente!=null) {
        	recensione.setUtente_Recensione(utente);
        	recensioneService.saveRecensione(recensione);
        }else {
            recensioneService.saveRecensione(recensione);
        }
        recensione.setEvento_Recensito(evento);
        model.addAttribute("recensione", recensione);
        return "redirect:/evento/" + evento.getId();

    }
    
    
    @PostMapping("/recensione/aggiungiRecensioneSito")
    private String AggiungiRecensioneSito(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("recensione") Recensione recensione, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            recensione.getFoto_recensione().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_recensioni";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        User utente= globalController.getCurrentUser();
        if(utente!=null) {
        	recensione.setUtente_Recensione(utente);
        	recensioneService.saveRecensione(recensione);
        }else {
            recensioneService.saveRecensione(recensione);
        }
        model.addAttribute("recensione", recensione);
        return "redirect:/" ;//TODO creare pagina recensioni sito
    }
    
    
    
    @GetMapping("/recensione/pagina_aggiungiRecensione_Evento/{eventoId}")
    private String PaginaAggiungiRecensioneEvento(@ModelAttribute("utente") User utente, @PathVariable("eventoId") Long eventoId, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("eventoId", eventoId);
        model.addAttribute("recensione", new Recensione());
        return "recensioneEvento";
    }

    @PostMapping("/recensione/aggiungiRecensioneEvento")
    private String AggiungiRecensioneEvento(@RequestParam("image") MultipartFile multipartFile, 
                                            @ModelAttribute("recensione") Recensione recensione,
                                            @RequestParam("eventoId") Long eventoId, 
                                            Model model) throws IOException {
        Evento evento = eventoService.getEventoById(eventoId);
        recensione.setEvento_Recensito(evento);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            recensione.getFoto_recensione().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_recensioni";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        User utente = globalController.getCurrentUser();
        if (utente != null) {
            recensione.setUtente_Recensione(utente);
            utente.getRecensioni_utente().add(recensione);
            
        }

        // Salva l'evento nel caso non fosse già salvato
        if (evento.getId() == null) {
            eventoService.saveEvento(evento);
        }

        recensioneService.saveRecensione(recensione);
        model.addAttribute("recensione", recensione);
        return "redirect:/";
    }


    
    
    
    
    
    
    
}
