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
import it.project.model.Fiore;
import it.project.model.User;
import it.project.service.FioreService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per la gestione delle operazioni sui fiori.
 * Gestisce la visualizzazione, aggiunta, modifica e rimozione dei fiori.
 */
@Controller
public class FioreController {

    @Autowired
    private FioreService fioreService;
    
    /**
     * Metodo per visualizzare la pagina di un singolo fiore.
     * 
     * @param fioreId ID del fiore da visualizzare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la visualizzazione del fiore.
     */
    @GetMapping("/fiore/{id}")
    private String PaginaFiore(@PathVariable("id") Long fioreId, @ModelAttribute("utente") User utente, Model model) {       
        model.addAttribute("fiore", this.fioreService.getFioreById(fioreId));
        model.addAttribute("utente", utente);
        return "fiore.html";
    }
    
    /**
     * Metodo per visualizzare la pagina di modifica di un fiore (solo per amministratori).
     * 
     * @param fioreId ID del fiore da modificare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la modifica del fiore.
     */
    @GetMapping("/admin/PaginaModificaFiore/{id}")
    private String PaginaModificaFiore(@PathVariable("id") Long fioreId, @ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("fiore", this.fioreService.getFioreById(fioreId));
        return "admin/pagine_fiore_amministratore/PaginaModificaFiore.html";
    }
    
    /**
     * Metodo per aggiornare le informazioni di un fiore esistente.
     * 
     * @param fiore  Oggetto Fiore con le informazioni aggiornate.
     * @param result Oggetto BindingResult per la gestione degli errori di validazione.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del fiore.
     */
    @PostMapping("/admin/PaginaModificaFiore/modifica")
    public String modificaFiore(@ModelAttribute("fiore") Fiore fiore, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("fiore", fiore);
            return "redirect:/admin/PaginaModificaFiore/" + fiore.getId();
        }

        // Carica il fiore originale dal database
        Fiore fioreOriginale = fioreService.getFioreById(fiore.getId());
        if (fioreOriginale != null) {
            // Mantieni le foto esistenti
            fiore.setFoto_fiore(fioreOriginale.getFoto_fiore());
            fioreService.saveFiore(fiore);
        }

        return "redirect:/admin/PaginaModificaFiore/" + fiore.getId();
    }

    /**
     * Metodo per visualizzare la pagina di aggiunta di un nuovo fiore (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per l'aggiunta di un fiore.
     */
    @GetMapping("/admin/pagina_aggiungiFiore")
    private String PaginaAggiungiFiore(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("fiore", new Fiore());
        return "admin/pagine_fiore_amministratore/PaginaAggiungiFiore.html";
    }
    
    /**
     * Metodo per aggiungere un nuovo fiore al sistema.
     * 
     * @param multipartFile Il file dell'immagine del fiore.
     * @param fiore         Oggetto Fiore con le informazioni del nuovo fiore.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del fiore appena aggiunto.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/aggiungiFiore")
    private String AggiungiFiore(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("fiore") Fiore fiore, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            fiore.getFoto_fiore().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_fiori";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        fioreService.saveFiore(fiore);
        model.addAttribute("utente", utente);
        model.addAttribute("fiore", fiore);
        return "redirect:/admin/PaginaModificaFiore/" + fiore.getId();
    }
    
    /**
     * Metodo per salvare una foto per un fiore esistente.
     * 
     * @param multipartFile Il file dell'immagine del fiore.
     * @param fioreId       ID del fiore a cui associare l'immagine.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del fiore.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/FioreSavePhoto/{fioreId}")
    public String saveFiorePhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable("fioreId") Long fioreId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Fiore fiore = fioreService.getFioreById(fioreId);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        fiore.getFoto_fiore().add(fileName);
        String uploadDir = "src/main/resources/static/images/foto_fiori";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        fioreService.saveFiore(fiore);
        model.addAttribute("utente", utente);
        model.addAttribute("fiore", fiore);
        return "redirect:/admin/PaginaModificaFiore/" + fiore.getId();
    }
    
    /**
     * Metodo per rimuovere una foto da un fiore esistente.
     * 
     * @param imageName Il nome dell'immagine da rimuovere.
     * @param fioreId   ID del fiore da cui rimuovere l'immagine.
     * @param model     Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del fiore.
     * @throws IOException In caso di errori durante l'eliminazione del file.
     */
    @PostMapping("/admin/fiore/removePhoto/{fioreId}")
    public String removeFiorePhoto(@RequestParam("image") String imageName, @PathVariable("fioreId") Long fioreId, Model model) throws IOException {
        Fiore fiore = fioreService.getFioreById(fioreId);
        if (fiore != null) {
            fiore.getFoto_fiore().remove(imageName);
            String uploadDir = "src/main/resources/static/images/foto_fiori";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            fioreService.saveFiore(fiore); // Assicurati di salvare le modifiche al fiore
            return "redirect:/admin/PaginaModificaFiore/" + fiore.getId();
        }
        return "redirect:/";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti i fiori.
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i fiori.
     */
    @GetMapping("/fiori")
    public String getAllFiori(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_fiori", fioreService.getAllFiori());
        model.addAttribute("tutte_categorie_fiori", fioreService.getDistinctCategories());
        model.addAttribute("utente", utente);
        return "fiori.html";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti i fiori (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i fiori.
     */
    @GetMapping("/admin/fiori")
    public String AdminGetAllFiori(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_fiori", fioreService.getAllFiori());
        model.addAttribute("utente", utente);
        return "admin/pagine_fiore_amministratore/fiori.html";
    }
    
    /**
     * Metodo per rimuovere un fiore dal sistema.
     * 
     * @param fioreId ID del fiore da rimuovere.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina con la lista di tutti i fiori.
     * @throws IOException In caso di errori durante l'eliminazione dei file.
     */
    @PostMapping("/admin/rimuoviFiore/{id}")
    public String removeFiore(@PathVariable("id") Long fioreId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Fiore fiore = fioreService.getFioreById(fioreId);
        List<String> fotoFiore = fiore.getFoto_fiore();
        String uploadDir = "src/main/resources/static/images/foto_fiori";
        for (String foto : fotoFiore) {
            FileUploadUtil.deleteFile(uploadDir, foto);
        }
        for (Evento evento : fiore.getEventi() ) {
        	evento.getFiori_evento().remove(fiore);
        }
        fioreService.deleteFiore(fioreId);
        model.addAttribute("utente", utente);
        return "redirect:/admin/fiori";
    }
}
