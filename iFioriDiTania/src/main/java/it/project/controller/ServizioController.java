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

import it.project.model.Servizio;
import it.project.model.User;
import it.project.service.ServizioService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per la gestione delle operazioni sui servizi.
 * Gestisce la visualizzazione, aggiunta, modifica e rimozione dei servizi.
 */
@Controller
public class ServizioController {

    @Autowired
    private ServizioService servizioService;
    
    /**
     * Metodo per visualizzare la pagina di un singolo servizio.
     * 
     * @param servizioId ID del servizio da visualizzare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la visualizzazione del servizio.
     */
    @GetMapping("/servizio/{id}")
    private String PaginaServizio(@PathVariable("id") Long servizioId, @ModelAttribute("utente") User utente, Model model) {       
        model.addAttribute("servizio", this.servizioService.getServizioById(servizioId));
        model.addAttribute("utente", utente);
        return "servizio.html";
    }
    
    /**
     * Metodo per visualizzare la pagina di modifica di un servizio (solo per amministratori).
     * 
     * @param servizioId ID del servizio da modificare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la modifica del servizio.
     */
    @GetMapping("/admin/PaginaModificaServizio/{id}")
    private String PaginaModificaServizio(@PathVariable("id") Long servizioId, @ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("servizio", this.servizioService.getServizioById(servizioId));
        return "admin/pagine_servizio_amministratore/PaginaModificaServizio.html";
    }
    
    /**
     * Metodo per aggiornare le informazioni di un servizio esistente.
     * 
     * @param servizio  Oggetto Servizio con le informazioni aggiornate.
     * @param result Oggetto BindingResult per la gestione degli errori di validazione.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del servizio.
     */
    @PostMapping("/admin/PaginaModificaServizio/modifica")
    public String modificaServizio(@ModelAttribute("servizio") Servizio servizio, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("servizio", servizio);
            return "redirect:/admin/PaginaModificaServizio/" + servizio.getId();
        }

        // Carica il servizio originale dal database
        Servizio servizioOriginale = servizioService.getServizioById(servizio.getId());
        if (servizioOriginale != null) {
            // Mantieni le foto esistenti
            servizio.setFoto_servizio(servizioOriginale.getFoto_servizio());
            servizioService.saveServizio(servizio);
        }

        return "redirect:/admin/PaginaModificaServizio/" + servizio.getId();
    }

    /**
     * Metodo per visualizzare la pagina di aggiunta di un nuovo servizio (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per l'aggiunta di un servizio.
     */
    @GetMapping("/admin/pagina_aggiungiServizio")
    private String PaginaAggiungiServizio(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("servizio", new Servizio());
        return "admin/pagine_servizio_amministratore/PaginaAggiungiServizio.html";
    }
    
    /**
     * Metodo per aggiungere un nuovo servizio al sistema.
     * 
     * @param multipartFile Il file dell'immagine del servizio.
     * @param servizio         Oggetto Servizio con le informazioni del nuovo servizio.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del servizio appena aggiunto.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/aggiungiServizio")
    private String AggiungiServizio(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("servizio") Servizio servizio, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            servizio.getFoto_servizio().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_servizi";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        servizioService.saveServizio(servizio);
        model.addAttribute("utente", utente);
        model.addAttribute("servizio", servizio);
        return "redirect:/admin/PaginaModificaServizio/" + servizio.getId();
    }
    
    /**
     * Metodo per salvare una foto per un servizio esistente.
     * 
     * @param multipartFile Il file dell'immagine del servizio.
     * @param servizioId       ID del servizio a cui associare l'immagine.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del servizio.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/ServiziosavePhoto/{servizioId}")
    public String saveServizioPhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable("servizioId") Long servizioId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Servizio servizio = servizioService.getServizioById(servizioId);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        servizio.getFoto_servizio().add(fileName);
        String uploadDir = "src/main/resources/static/images/foto_servizi";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        servizioService.saveServizio(servizio);
        model.addAttribute("utente", utente);
        model.addAttribute("servizio", servizio);
        return "redirect:/admin/PaginaModificaServizio/" + servizio.getId();
    }
    
    /**
     * Metodo per rimuovere una foto da un servizio esistente.
     * 
     * @param imageName Il nome dell'immagine da rimuovere.
     * @param servizioId   ID del servizio da cui rimuovere l'immagine.
     * @param model     Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del servizio.
     * @throws IOException In caso di errori durante l'eliminazione del file.
     */
    @PostMapping("/admin/servizio/removePhoto/{servizioId}")
    public String removeServizioPhoto(@RequestParam("image") String imageName, @PathVariable("servizioId") Long servizioId, Model model) throws IOException {
        Servizio servizio = servizioService.getServizioById(servizioId);
        if (servizio != null) {
            servizio.getFoto_servizio().remove(imageName);
            String uploadDir = "src/main/resources/static/images/foto_servizi";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            servizioService.saveServizio(servizio); // Assicurati di salvare le modifiche al servizio
            return "redirect:/admin/PaginaModificaServizio/" + servizio.getId();
        }
        return "redirect:/";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti i servizi.
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i servizi.
     */
    @GetMapping("/servizi")
    public String getAllServizi(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_servizi", servizioService.getAllServizi());
        model.addAttribute("utente", utente);
        return "servizi.html";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti i servizi (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i servizi.
     */
    @GetMapping("/admin/servizi")
    public String AdminGetAllServizi(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_servizi", servizioService.getAllServizi());
        model.addAttribute("utente", utente);
        return "admin/pagine_servizio_amministratore/servizi.html";
    }
    
    /**
     * Metodo per rimuovere un servizio dal sistema.
     * 
     * @param servizioId ID del servizio da rimuovere.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina con la lista di tutti i servizi.
     * @throws IOException In caso di errori durante l'eliminazione dei file.
     */
    @PostMapping("/admin/rimuoviServizio/{id}")
    public String removeServizio(@PathVariable("id") Long servizioId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Servizio servizio = servizioService.getServizioById(servizioId);
        List<String> fotoServizio = servizio.getFoto_servizio();
        String uploadDir = "src/main/resources/static/images/foto_servizi";
        for (String foto : fotoServizio) {
            FileUploadUtil.deleteFile(uploadDir, foto);
        }
        
        servizioService.deleteById(servizioId);
        model.addAttribute("utente", utente);
        return "redirect:/admin/servizi";
    }
}
