package it.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.project.model.User;
import it.project.service.CredentialsService;
import it.project.service.UserService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per gestire le operazioni relative agli utenti.
 */
@Controller
public class UtenteController {

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalController globalController;

    @Autowired
    private CredentialsService credentialsService;



    /**
     * Metodo per ottenere tutti gli utenti .
     * 
     * @param model il modello della vista
     * @return il nome della vista che mostra tutti gli utenti 
     */
    @GetMapping("/utenti")
    public String getAllUser(Model model) {
        model.addAttribute("tutti_utenti", userService.getAllUsers());
        return "utenti.html";
    }

    /**
     * Metodo per ottenere un utente specifico.
     * 
     * @param cuocoId l'ID dell'utente
     * @param model il modello della vista
     * @return il nome della vista che mostra l'utente
     */
    @GetMapping("/utente/{utenteId}")
    public String getCuoco(@PathVariable("utenteId") Long utenteId, Model model) {
        model.addAttribute("utente", userService.getUserById(utenteId));
        return "utente.html";
    }

    /**
     * Metodo per salvare la foto del profilo di un utente.
     * 
     * @param multipartFile il file dell'immagine
     * @param redirectAttributes attributi per la redirezione
     * @return la redirezione alla vista utente
     * @throws IOException in caso di errore durante il salvataggio del file
     */
    @PostMapping("/user/savePhoto")
    public String saveUserPhoto(@RequestParam("image") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
        if (multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/user";
        }

        // Ottieni l'utente autenticato
        User user = globalController.getCurrentUser();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPicture(fileName);

        // Salva l'utente aggiornato
        userService.saveUser(user);

        String uploadDir = "src/main/resources/static/images";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + fileName + "'");
        return "redirect:/user";
    }

    /**
     * Metodo per rimuovere la foto del profilo di un utente.
     * 
     * @param id l'ID dell'utente
     * @param model il modello della vista
     * @return il nome della vista principale
     */
    @RequestMapping(value = "/admin/rimuoviFotoProfilo/{id}", method = RequestMethod.GET)
    public String removeUserPhoto(@PathVariable("id") Long id, Model model) {
        String fileName = userService.getUserById(id).getPicture();
        String uploadDir = "src/main/resources/static/images";
        try {
            FileUploadUtil.deleteFile(uploadDir, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        userService.saveUser(userService.getUserById(id));
        return "index";
    }

    /**
     * Metodo per ottenere tutti gli utenti per l'amministratore.
     * 
     * @param model il modello della vista
     * @return il nome della vista che mostra tutti gli utenti per l'amministratore
     */
    @RequestMapping(value = "/admin/all_user", method = RequestMethod.GET)
    public String getAllUserAdmin(Model model) {
        model.addAttribute("tutti_utenti", userService.getAllUsers());
        return "admin/pagine_utente_amministratore/utenti.html";
    }

    /**
     * Metodo per rimuovere un utente e le sue ricette per l'amministratore.
     * 
     * @param id l'ID dell'utente
     * @param model il modello della vista
     * @return il nome della vista che mostra tutti gli utenti per l'amministratore
     */
    @PostMapping("/admin/remove/user/{utenteid}")
    public String removeUserAdmin(@PathVariable("utenteid") Long id, Model model) {
        User user = userService.getUserById(id);

        credentialsService.deleteByUser(user);
        model.addAttribute("tutti_utenti", userService.getAllUsers());
        return "admin/pagine_utente_amministratore/utenti.html";
    }
}
