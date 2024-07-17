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
import it.project.model.Evento;
import it.project.model.User;
import it.project.service.AccessorioService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per la gestione delle operazioni sugli accessori.
 * Gestisce la visualizzazione, aggiunta, modifica e rimozione degli accessori.
 */
@Controller
public class AccessorioController {

    @Autowired
    private AccessorioService accessorioService;
    
    /**
     * Metodo per visualizzare la pagina di un singolo accessorio.
     * 
     * @param accessorioId ID dell'accessorio da visualizzare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la visualizzazione dell'accessorio.
     */
    @GetMapping("/accessorio/{id}")
    private String PaginaAccessorio(@PathVariable("id") Long accessorioId, @ModelAttribute("utente") User utente, Model model) {       
        model.addAttribute("accessorio", this.accessorioService.getAccessorioById(accessorioId));
        model.addAttribute("utente", utente);
        return "accessorio.html";
    }
    
    /**
     * Metodo per visualizzare la pagina di modifica di un accessorio (solo per amministratori).
     * 
     * @param accessorioId ID dell'accessorio da modificare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la modifica dell'accessorio.
     */
    @GetMapping("/admin/PaginaModificaAccessorio/{id}")
    private String PaginaModificaAccessorio(@PathVariable("id") Long accessorioId, @ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("accessorio", this.accessorioService.getAccessorioById(accessorioId));
        return "admin/pagine_accessorio_amministratore/PaginaModificaAccessorio.html";
    }
    
    /**
     * Metodo per aggiornare le informazioni di un accessorio esistente.
     * 
     * @param accessorio  Oggetto Accessorio con le informazioni aggiornate.
     * @param result Oggetto BindingResult per la gestione degli errori di validazione.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica dell'accessorio.
     */
    @PostMapping("/admin/PaginaModificaAccessorio/modifica")
    public String modificaAccessorio(@ModelAttribute("accessorio") Accessorio accessorio, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("accessorio", accessorio);
            return "redirect:/admin/PaginaModificaAccessorio/" + accessorio.getId();
        }

        // Carica l'accessorio originale dal database
        Accessorio accessorioOriginale = accessorioService.getAccessorioById(accessorio.getId());
        if (accessorioOriginale != null) {
            // Mantieni le foto esistenti
            accessorio.setFoto_accessorio(accessorioOriginale.getFoto_accessorio());
            accessorioService.saveAccessorio(accessorio);
        }

        return "redirect:/admin/PaginaModificaAccessorio/" + accessorio.getId();
    }

    /**
     * Metodo per visualizzare la pagina di aggiunta di un nuovo accessorio (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per l'aggiunta di un accessorio.
     */
    @GetMapping("/admin/pagina_aggiungiAccessorio")
    private String PaginaAggiungiAccessorio(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("accessorio", new Accessorio());
        return "admin/pagine_accessorio_amministratore/PaginaAggiungiAccessorio.html";
    }
    
    /**
     * Metodo per aggiungere un nuovo accessorio al sistema.
     * 
     * @param multipartFile Il file dell'immagine dell'accessorio.
     * @param accessorio         Oggetto Accessorio con le informazioni del nuovo accessorio.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica dell'accessorio appena aggiunto.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/aggiungiAccessorio")
    private String AggiungiAccessorio(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("accessorio") Accessorio accessorio, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            accessorio.getFoto_accessorio().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_accessori";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        accessorioService.saveAccessorio(accessorio);
        model.addAttribute("utente", utente);
        model.addAttribute("accessorio", accessorio);
        return "redirect:/admin/PaginaModificaAccessorio/" + accessorio.getId();
    }
    
    /**
     * Metodo per salvare una foto per un accessorio esistente.
     * 
     * @param multipartFile Il file dell'immagine dell'accessorio.
     * @param accessorioId       ID dell'accessorio a cui associare l'immagine.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica dell'accessorio.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/AccessoriosavePhoto/{accessorioId}")
    public String saveAccessorioPhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable("accessorioId") Long accessorioId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Accessorio accessorio = accessorioService.getAccessorioById(accessorioId);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        accessorio.getFoto_accessorio().add(fileName);
        String uploadDir = "src/main/resources/static/images/foto_accessori";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        accessorioService.saveAccessorio(accessorio);
        model.addAttribute("utente", utente);
        model.addAttribute("accessorio", accessorio);
        return "redirect:/admin/PaginaModificaAccessorio/" + accessorio.getId();
    }
    
    /**
     * Metodo per rimuovere una foto da un accessorio esistente.
     * 
     * @param imageName Il nome dell'immagine da rimuovere.
     * @param accessorioId   ID dell'accessorio da cui rimuovere l'immagine.
     * @param model     Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica dell'accessorio.
     * @throws IOException In caso di errori durante l'eliminazione del file.
     */
    @PostMapping("/admin/accessorio/removePhoto/{accessorioId}")
    public String removeAccessorioPhoto(@RequestParam("image") String imageName, @PathVariable("accessorioId") Long accessorioId, Model model) throws IOException {
        Accessorio accessorio = accessorioService.getAccessorioById(accessorioId);
        if (accessorio != null) {
            accessorio.getFoto_accessorio().remove(imageName);
            String uploadDir = "src/main/resources/static/images/foto_accessori";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            accessorioService.saveAccessorio(accessorio); // Assicurati di salvare le modifiche all'accessorio
            return "redirect:/admin/PaginaModificaAccessorio/" + accessorio.getId();
        }
        return "redirect:/";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti gli accessori.
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti gli accessori.
     */
    @GetMapping("/accessori")
    public String getAllAccessori(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_accessori", accessorioService.getAllAccessori());
        model.addAttribute("tutte_categorie_accessori", accessorioService.getDistinctCategories());
        model.addAttribute("utente", utente);
        return "accessori.html";
    }

    /**
     * Metodo per visualizzare la pagina con la lista di tutti gli accessori (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la lista di tutti gli accessori.
     */
    @GetMapping("/admin/accessori")
    public String AdminGetAllAccessori(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_accessori", accessorioService.getAllAccessori());
        model.addAttribute("utente", utente);
        return "admin/pagine_accessorio_amministratore/accessori.html";
    }
    
    /**
     * Metodo per rimuovere un accessorio dal sistema.
     * 
     * @param accessorioId ID dell'accessorio da rimuovere.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina con la lista di tutti gli accessori.
     * @throws IOException In caso di errori durante l'eliminazione dei file.
     */
    @PostMapping("/admin/rimuoviAccessorio/{id}")
    public String removeAccessorio(@PathVariable("id") Long accessorioId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Accessorio accessorio = accessorioService.getAccessorioById(accessorioId);
        List<String> fotoAccessorio = accessorio.getFoto_accessorio();
        String uploadDir = "src/main/resources/static/images/foto_accessori";
        for (String foto : fotoAccessorio) {
            FileUploadUtil.deleteFile(uploadDir, foto);
        }
        for (Evento evento : accessorio.getEventi() ) {
        	evento.getAccessori_evento().remove(accessorio);
        }
        accessorioService.deleteAccessorio(accessorioId);
        model.addAttribute("utente", utente);
        return "redirect:/admin/accessori";
    }
}
