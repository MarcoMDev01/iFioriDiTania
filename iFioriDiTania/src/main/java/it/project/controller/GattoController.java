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

import it.project.model.Gatto;
import it.project.model.User;
import it.project.service.GattoService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per la gestione delle operazioni sui gatti.
 * Gestisce la visualizzazione, aggiunta, modifica e rimozione dei gatti.
 */
@Controller
public class GattoController {

    @Autowired
    private GattoService gattoService;
    
    /**
     * Metodo per visualizzare la pagina di un singolo gatto.
     * 
     * @param gattoId ID del gatto da visualizzare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la visualizzazione del gatto.
     */
    @GetMapping("/gatto/{id}")
    private String PaginaGatto(@PathVariable("id") Long gattoId, @ModelAttribute("utente") User utente, Model model) {       
        model.addAttribute("gatto", this.gattoService.getGattoById(gattoId));
        model.addAttribute("utente", utente);
        return "gatto.html";
    }
    
    /**
     * Metodo per visualizzare la pagina di modifica di un gatto (solo per amministratori).
     * 
     * @param gattoId ID del gatto da modificare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la modifica del gatto.
     */
    @GetMapping("/admin/PaginaModificaGatto/{id}")
    private String PaginaModificaGatto(@PathVariable("id") Long gattoId, @ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("gatto", this.gattoService.getGattoById(gattoId));
        return "admin/pagine_gatto_amministratore/PaginaModificaGatto.html";
    }
    
    /**
     * Metodo per aggiornare le informazioni di un gatto esistente.
     * 
     * @param gatto  Oggetto Gatto con le informazioni aggiornate.
     * @param result Oggetto BindingResult per la gestione degli errori di validazione.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del gatto.
     */
    @PostMapping("/admin/PaginaModificaGatto/modifica")
    public String modificaGatto(@ModelAttribute("gatto") Gatto gatto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("gatto", gatto);
            return "redirect:/admin/PaginaModificaGatto/" + gatto.getId();
        }

        // Carica il gatto originale dal database
        Gatto gattoOriginale = gattoService.getGattoById(gatto.getId());
        if (gattoOriginale != null) {
            // Mantieni le foto esistenti
            gatto.setFoto_gatto(gattoOriginale.getFoto_gatto());
            gattoService.saveGatto(gatto);
        }

        return "redirect:/admin/PaginaModificaGatto/" + gatto.getId();
    }

    /**
     * Metodo per visualizzare la pagina di aggiunta di un nuovo gatto (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per l'aggiunta di un gatto.
     */
    @GetMapping("/admin/pagina_aggiungiGatto")
    private String PaginaAggiungiGatto(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("gatto", new Gatto());
        return "admin/pagine_gatto_amministratore/PaginaAggiungiGatto.html";
    }
    
    /**
     * Metodo per aggiungere un nuovo gatto al sistema.
     * 
     * @param multipartFile Il file dell'immagine del gatto.
     * @param gatto         Oggetto Gatto con le informazioni del nuovo gatto.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del gatto appena aggiunto.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/aggiungiGatto")
    private String AggiungiGatto(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("gatto") Gatto gatto, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            gatto.getFoto_gatto().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_gatti";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        gattoService.saveGatto(gatto);
        model.addAttribute("utente", utente);
        model.addAttribute("gatto", gatto);
        return "redirect:/admin/PaginaModificaGatto/" + gatto.getId();
    }
    
    /**
     * Metodo per salvare una foto per un gatto esistente.
     * 
     * @param multipartFile Il file dell'immagine del gatto.
     * @param gattoId       ID del gatto a cui associare l'immagine.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del gatto.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/GattosavePhoto/{gattoId}")
    public String saveGattoPhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable("gattoId") Long gattoId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Gatto gatto = gattoService.getGattoById(gattoId);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        gatto.getFoto_gatto().add(fileName);
        String uploadDir = "src/main/resources/static/images/foto_gatti";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        gattoService.saveGatto(gatto);
        model.addAttribute("utente", utente);
        model.addAttribute("gatto", gatto);
        return "redirect:/admin/PaginaModificaGatto/" + gatto.getId();
    }
    
    /**
     * Metodo per rimuovere una foto da un gatto esistente.
     * 
     * @param imageName Il nome dell'immagine da rimuovere.
     * @param gattoId   ID del gatto da cui rimuovere l'immagine.
     * @param model     Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del gatto.
     * @throws IOException In caso di errori durante l'eliminazione del file.
     */
    @PostMapping("/admin/gatto/removePhoto/{gattoId}")
    public String removeGattoPhoto(@RequestParam("image") String imageName, @PathVariable("gattoId") Long gattoId, Model model) throws IOException {
        Gatto gatto = gattoService.getGattoById(gattoId);
        if (gatto != null) {
            gatto.getFoto_gatto().remove(imageName);
            String uploadDir = "src/main/resources/static/images/foto_gatti";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            gattoService.saveGatto(gatto); // Assicurati di salvare le modifiche al gatto
            return "redirect:/admin/PaginaModificaGatto/" + gatto.getId();
        }
        return "redirect:/";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti i gatti.
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i gatti.
     */
    @GetMapping("/gatti")
    public String getAllGatti(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_gatti", gattoService.getAllGatti());
        model.addAttribute("utente", utente);
        return "gatti.html";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti i gatti (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti i gatti.
     */
    @GetMapping("/admin/gatti")
    public String AdminGetAllGatti(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_gatti", gattoService.getAllGatti());
        model.addAttribute("utente", utente);
        return "admin/pagine_gatto_amministratore/gatti.html";
    }
    
    /**
     * Metodo per rimuovere un gatto dal sistema.
     * 
     * @param gattoId ID del gatto da rimuovere.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina con la lista di tutti i gatti.
     * @throws IOException In caso di errori durante l'eliminazione dei file.
     */
    @PostMapping("/admin/rimuoviGatto/{id}")
    public String removeGatto(@PathVariable("id") Long gattoId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Gatto gatto = gattoService.getGattoById(gattoId);
        List<String> fotoGatto = gatto.getFoto_gatto();
        String uploadDir = "src/main/resources/static/images/foto_gatti";
        for (String foto : fotoGatto) {
            FileUploadUtil.deleteFile(uploadDir, foto);
        }
        
        gattoService.deleteById(gattoId);
        model.addAttribute("utente", utente);
        return "redirect:/admin/gatti";
    }
}
