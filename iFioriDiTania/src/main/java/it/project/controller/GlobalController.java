package it.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.project.model.User;
import it.project.service.CredentialsService;
import it.project.service.UserService;


/**
 * GlobalController è un controller globale che fornisce metodi per aggiungere dati relativi agli utenti al modello per tutte le viste.
 * Ciò garantisce che i dettagli dell'utente e i dati dell'utente siano disponibili in tutti i template.
 * 
 * @see ControllerAdvice
 * @see ModelAttribute
 */
@ControllerAdvice
public class GlobalController {
	
	@Autowired
	private CredentialsService credentialsService;

    @Autowired
	private UserService userService;
    

    /**
     * Aggiunge i dettagli dell'utente (principal) al modello se l'utente è autenticato.
     * Questo metodo fornisce l'attributo 'userDetails' per tutti i template.
     * 
     * @return i dettagli dell'utente autenticato o null se l'utente non è autenticato
     */
    @ModelAttribute("userDetails")
    public Object getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return principal;
            } else if (principal instanceof DefaultOidcUser) {
                return principal;
            }
        }
        return null;
    }
    
    /**
     * Aggiunge l'entità User corrente al modello se l'utente è autenticato.
     * Questo metodo fornisce l'attributo 'userData' per tutti i template.
     * 
     * @return l'entità User autenticata o null se l'utente non è autenticato
     */
    @ModelAttribute("userData")
    public User getCurrentUser() {
        // Ottiene l'oggetto Authentication dalla SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se l'autenticazione è anonima
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Se l'utente è anonimo, ritorna null
            return null;
        }

        // Ottiene il principal (l'utente autenticato)
        Object principal = authentication.getPrincipal();
        User utente = null;

        // Verifica se il principal è un'istanza di UserDetails
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            utente = credentialsService.getUserByCredentials(userDetails.getUsername());
        } 
        // Verifica se il principal è un'istanza di DefaultOidcUser
        else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            utente = userService.getCustomOAuth2UserByEmailAndProvider(oidcUser.getEmail(), "google");
        }

        // Ritorna l'utente autenticato o null se non è autenticato
        return utente;
    }
    
    
    
}
