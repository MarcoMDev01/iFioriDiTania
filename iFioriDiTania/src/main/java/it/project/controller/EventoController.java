package it.project.controller;

import java.io.IOException;
import java.util.Optional;
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
import it.project.model.User;
import it.project.model.Fiore;
import it.project.model.Mazzo;
import it.project.model.Servizio;
import it.project.model.Accessorio;
import it.project.model.Recensione;
import it.project.service.EventoService;
import it.project.utils.FileUploadUtil;

/**
 * Controller per la gestione delle operazioni sugli eventi.
 * Gestisce la visualizzazione, aggiunta, modifica e rimozione degli eventi.
 */
@Controller
public class EventoController {

    @Autowired
    private EventoService eventoService;
    
    /**
     * Metodo per visualizzare la pagina di un singolo evento.
     * 
     * @param eventoId ID del evento da visualizzare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la visualizzazione del evento.
     */
    @GetMapping("/evento/{id}")
    private String PaginaEvento(@PathVariable("id") Long eventoId, @ModelAttribute("utente") User utente, Model model) {       
        model.addAttribute("evento", this.eventoService.getEventoById(eventoId));
        model.addAttribute("utente", utente);
        return "evento.html";
    }
    
    /**
     * Metodo per visualizzare la pagina di modifica di un evento (solo per amministratori).
     * 
     * @param eventoId ID del evento da modificare.
     * @param utente  Oggetto utente da associare al modello.
     * @param model   Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per la modifica del evento.
     */
    @GetMapping("/admin/PaginaModificaEvento/{id}")
    private String PaginaModificaEvento(@PathVariable("id") Long eventoId, @ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("evento", this.eventoService.getEventoById(eventoId));
        return "admin/pagine_evento_amministratore/PaginaModificaEvento.html";
    }
    
    /**
     * Metodo per aggiornare le informazioni di un evento esistente.
     * 
     * @param evento  Oggetto Evento con le informazioni aggiornate.
     * @param result Oggetto BindingResult per la gestione degli errori di validazione.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del evento.
     */
    @PostMapping("/admin/PaginaModificaEvento/modifica")
    public String modificaEvento(@ModelAttribute("evento") Evento evento, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("evento", evento);
            return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
        }

        // Carica il evento originale dal database
        Evento eventoOriginale = eventoService.getEventoById(evento.getId());
        if (eventoOriginale != null) {
            // Mantieni le foto esistenti
            evento.setFoto_evento(eventoOriginale.getFoto_evento());
            eventoService.saveEvento(evento);
        }

        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    /**
     * Metodo per visualizzare la pagina di aggiunta di un nuovo evento (solo per amministratori).
     * 
     * @param utente Oggetto utente da associare al modello.
     * @param model  Oggetto Model per passare attributi alla vista.
     * @return Il nome del template HTML per l'aggiunta di un evento.
     */
    @GetMapping("/admin/pagina_aggiungiEvento")
    private String PaginaAggiungiEvento(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("utente", utente);
        model.addAttribute("evento", new Evento());
        return "admin/pagine_evento_amministratore/PaginaAggiungiEvento.html";
    }
    
    /**
     * Metodo per aggiungere un nuovo evento al sistema.
     * 
     * @param multipartFile Il file dell'immagine del evento.
     * @param evento        Oggetto Evento con le informazioni del nuovo evento.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del evento appena aggiunto.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/aggiungiEvento")
    private String AggiungiEvento(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            evento.getFoto_evento().add(fileName);
            String uploadDir = "src/main/resources/static/images/foto_eventi";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        eventoService.saveEvento(evento);
        model.addAttribute("utente", utente);
        model.addAttribute("evento", evento);
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }
    
    /**
     * Metodo per salvare una foto per un evento esistente.
     * 
     * @param multipartFile Il file dell'immagine del evento.
     * @param eventoId      ID del evento a cui associare l'immagine.
     * @param utente        Oggetto utente da associare al modello.
     * @param model         Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del evento.
     * @throws IOException In caso di errori durante il salvataggio del file.
     */
    @PostMapping("/admin/EventosavePhoto/{eventoId}")
    public String saveEventoPhoto(@RequestParam("image") MultipartFile multipartFile, @PathVariable("eventoId") Long eventoId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Evento evento = eventoService.getEventoById(eventoId);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        evento.getFoto_evento().add(fileName);
        String uploadDir = "src/main/resources/static/images/foto_eventi";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        eventoService.saveEvento(evento);
        model.addAttribute("utente", utente);
        model.addAttribute("evento", evento);
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }
    
    /**
     * Metodo per rimuovere una foto da un evento esistente.
     * 
     * @param imageName Il nome dell'immagine da rimuovere.
     * @param eventoId  ID del evento da cui rimuovere l'immagine.
     * @param model     Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina di modifica del evento.
     * @throws IOException In caso di errori durante l'eliminazione del file.
     */
    @PostMapping("/admin/evento/removePhoto/{eventoId}")
    public String removeEventoPhoto(@RequestParam("image") String imageName, @PathVariable("eventoId") Long eventoId, Model model) throws IOException {
        Evento evento = eventoService.getEventoById(eventoId);
        if (evento != null) {
            evento.getFoto_evento().remove(imageName);
            String uploadDir = "src/main/resources/static/images/foto_eventi";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            eventoService.saveEvento(evento); // Assicurati di salvare le modifiche al evento
            return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
        }
        return "redirect:/";
    }

    @PostMapping("/admin/eventosavePhoto")
    public String saveEventoPhoto(@RequestParam("image") MultipartFile multipartFile, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (evento != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            evento.getFoto_evento().add(fileName);
            
            eventoService.saveEvento(evento);

            String uploadDir = "src/main/resources/static/images";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
        }
        return "redirect:/";
    }
    
    @PostMapping("/admin/evento/removePhoto")
    public String removeEventoPhoto(@RequestParam("image") String imageName, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente, Model model) throws IOException {
        if (evento != null) {
            evento.getFoto_evento().remove(imageName);

            eventoService.saveEvento(evento);

            String uploadDir = "src/main/resources/static/images";
            FileUploadUtil.deleteFile(uploadDir, imageName);
            return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
        }
        return "redirect:/";
    }

    @PostMapping("/admin/evento/addFiore")
    public String addFioreToEvento(@RequestParam("fioreId") Long fioreId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Fiore> fiore = eventoService.getFioreById(fioreId);
        if (fiore.isPresent()) {
            evento.getFiori_evento().add(fiore.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/removeFiore")
    public String removeFioreFromEvento(@RequestParam("fioreId") Long fioreId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Fiore> fiore = eventoService.getFioreById(fioreId);
        if (fiore.isPresent()) {
            evento.getFiori_evento().remove(fiore.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/addMazzo")
    public String addMazzoToEvento(@RequestParam("mazzoId") Long mazzoId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Mazzo> mazzo = eventoService.getMazzoById(mazzoId);
        if (mazzo.isPresent()) {
            evento.getMazzi_evento().add(mazzo.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/removeMazzo")
    public String removeMazzoFromEvento(@RequestParam("mazzoId") Long mazzoId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Mazzo> mazzo = eventoService.getMazzoById(mazzoId);
        if (mazzo.isPresent()) {
            evento.getMazzi_evento().remove(mazzo.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/addServizio")
    public String addServizioToEvento(@RequestParam("servizioId") Long servizioId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Servizio> servizio = eventoService.getServizioById(servizioId);
        if (servizio.isPresent()) {
            evento.getServizi_evento().add(servizio.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/removeServizio")
    public String removeServizioFromEvento(@RequestParam("servizioId") Long servizioId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Servizio> servizio = eventoService.getServizioById(servizioId);
        if (servizio.isPresent()) {
            evento.getServizi_evento().remove(servizio.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/addAccessorio")
    public String addAccessorioToEvento(@RequestParam("accessorioId") Long accessorioId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Accessorio> accessorio = eventoService.getAccessorioById(accessorioId);
        if (accessorio.isPresent()) {
            evento.getAccessori_evento().add(accessorio.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/removeAccessorio")
    public String removeAccessorioFromEvento(@RequestParam("accessorioId") Long accessorioId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Accessorio> accessorio = eventoService.getAccessorioById(accessorioId);
        if (accessorio.isPresent()) {
            evento.getAccessori_evento().remove(accessorio.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/addRecensione")
    public String addRecensioneToEvento(@RequestParam("recensioneId") Long recensioneId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Recensione> recensione = eventoService.getRecensioneById(recensioneId);
        if (recensione.isPresent()) {
            evento.getRecensioni_evento().add(recensione.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @PostMapping("/admin/evento/removeRecensione")
    public String removeRecensioneFromEvento(@RequestParam("recensioneId") Long recensioneId, @ModelAttribute("evento") Evento evento, @ModelAttribute("utente") User utente) {
        Optional<Recensione> recensione = eventoService.getRecensioneById(recensioneId);
        if (recensione.isPresent()) {
            evento.getRecensioni_evento().remove(recensione.get());
            eventoService.saveEvento(evento);
        }
        return "redirect:/admin/PaginaModificaEvento/" + evento.getId();
    }

    @GetMapping("/eventi")
    public String getAllEventi(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_eventi", eventoService.getAllEventi());
        model.addAttribute("utente", utente);
        return "eventi.html";
    }

    @GetMapping("/admin/eventi")
    public String adminGetAllEventi(@ModelAttribute("utente") User utente, Model model) {
        model.addAttribute("tutti_eventi", eventoService.getAllEventi());
        model.addAttribute("utente", utente);
        return "admin/pagine_evento_amministratore/eventi.html";
    }

    /**
     * Metodo per rimuovere un evento dal sistema.
     * 
     * @param eventoId ID del evento da rimuovere.
     * @param utente   Oggetto utente da associare al modello.
     * @param model    Oggetto Model per passare attributi alla vista.
     * @return Redirect alla pagina con la lista di tutti gli eventi.
     * @throws IOException In caso di errori durante l'eliminazione dei file.
     */
    @PostMapping("/admin/rimuoviEvento/{id}")
    public String removeEvento(@PathVariable("id") Long eventoId, @ModelAttribute("utente") User utente, Model model) throws IOException {
        Evento evento = eventoService.getEventoById(eventoId);
        List<String> fotoEvento = evento.getFoto_evento();
        String uploadDir = "src/main/resources/static/images/foto_eventi";
        for (String foto : fotoEvento) {
            FileUploadUtil.deleteFile(uploadDir, foto);
        }
        
        eventoService.deleteById(eventoId);
        model.addAttribute("utente", utente);
        return "redirect:/admin/eventi";
    }
}
