package it.project.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import it.project.model.*;
import it.project.service.CredentialsService;
import it.project.service.FioreService;
import it.project.service.MazzoService;
import it.project.service.RecensioneService;
import it.project.service.ServizioService;
import it.project.service.UserService;
import it.project.model.Credentials;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;

    @Autowired
	private UserService userService;
    @Autowired
	private FioreService fioreService ;
    @Autowired
	private ServizioService servizioService ;
    @Autowired
	private MazzoService mazzoService ;
    @Autowired
	private RecensioneService recensioneService ;
    

    @Autowired
    private GlobalController globalController;
    
    
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}


/*
	@GetMapping(value = "/")
	public String index(Model model) {
	    // Ottiene l'oggetto Authentication dalla SecurityContextHolder
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    User utente = null;

	    // Verifica se l'autenticazione è anonima
	    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	        // Se l'utente è anonimo, ritorna la pagina index
	        return "index";
	    } else {
	        utente = globalController.getCurrentUser();	
	        Credentials credenziali = credentialsService.getUserByCredentialsUser(utente);
            model.addAttribute("user", utente);
            model.addAttribute("user_id", utente.getId());
	        if(credenziali.getRole().equals(Credentials.ADMIN_ROLE)) {
                // Se l'utente è un amministratore, ritorna la pagina admin/indexAdmin
                return "admin/indexAdmin"; 
	        }
	        else {
	        	return "index";
	        }
	        
	    }

	}
	*/
	/** 
	 * se trova un utente oauth controlla se è salvato altrimenti lo memorizza  
	 * @param authentication
	 * @return direziona verso il metodo index, esso gestira' se l'utente è admin o meno 
	 * 
	 * *//*
	@GetMapping("/success")
	public String defaultAfterLogin(Authentication authentication) {
		
		if (authentication.getPrincipal() instanceof DefaultOidcUser ) {
		        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
		        String email = oidcUser.getEmail();
		        
            if (!credentialsService.existsByUsername(email) && !userService.existsByEmailAndProvider(email, "google")) {
		            // Salva l'utente e salva credenziali connesse
		        	userService.saveOauthUser(oidcUser);
		        	User utenteOauth= userService.getCustomOAuth2UserByEmail(email);
		        	credentialsService.saveCredentialsFromOidcUser(utenteOauth);
		        	
		        }
		}
		  
	    return "redirect:/"; // Redirezione di default
	}
*/
 
    /**
     * Metodo per gestire la home page dopo il login.
     * 
     * @param model il modello della vista
     * @return il nome della vista della home page
     */
    @GetMapping(value = "/")
    public String index(Model model) {
        // Ottiene l'oggetto Authentication dalla SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User utente = null;
        model.addAttribute("tutti_fiori", fioreService.getAllFiori());
        model.addAttribute("tutti_servizi", servizioService.getAllServizi());
        model.addAttribute("tutti_mazzi", mazzoService.getAllMazzi());
        model.addAttribute("tutte_recensioni_sito", recensioneService.getRecensioniByEventoRecensitoIsNullAndApprovazioneIsTrue());
        model.addAttribute("tutte_categorie_fiori", fioreService.getDistinctCategories());
        // Verifica se l'autenticazione è anonima
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Se l'utente è anonimo, ritorna la pagina index
            return "index";
        } else {
            // Se l'utente è autenticato, ottiene il principal (l'utente autenticato)
            Object principal = authentication.getPrincipal();

            // Verifica se il principal è un'istanza di UserDetails (un utente autenticato tramite username e password)
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                // Ottiene le credenziali dell'utente (ad esempio, ruoli e permessi)
                Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

                // Verifica se l'utente ha il ruolo di amministratore
                if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                    // Se l'utente è un amministratore, ritorna la pagina admin/indexAdmin
                    return "admin/indexAdmin";
                }
                utente = credentialsService.getUserByCredentials(userDetails.getUsername());
                if (!"google".equals(utente.getProvider())) {
                    model.addAttribute("user", utente);
                    model.addAttribute("user_id", utente.getId());
                }
            }
            // Verifica se il principal è un'istanza di DefaultOidcUser (un utente autenticato tramite OpenID Connect)
            else if (authentication instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
                utente = userService.getCustomOAuth2UserByEmailAndProvider(oidcUser.getEmail(), "google");
                String email = oidcUser.getEmail();
                // Modifica: Controlla se l'utente esiste con il provider "google", se no lo crea
                if (utente == null) {
                    // Salva l'utente nelle credenziali e negli utenti OAuth
                    userService.saveOauthUser(oidcUser);
                    User utenteOauth = userService.getCustomOAuth2UserByEmail(email);
                    credentialsService.saveCredentialsFromOidcUser(utenteOauth);
                    utente = userService.getCustomOAuth2UserByEmailAndProvider(oidcUser.getEmail(), "google");
                }

                model.addAttribute("user", utente);
                model.addAttribute("user_id", utente.getId());
            }
        }

        // Se nessuna delle condizioni precedenti è soddisfatta, ritorna la pagina index
        return "index";
    }

    /**
     * Metodo per gestire la redirezione dopo il login con successo.
     * 
     * @param authentication l'oggetto di autenticazione
     * @return la redirezione alla vista appropriata
     */
    @GetMapping("/success")
    public String defaultAfterLogin(Authentication authentication) {
        if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            String email = oidcUser.getEmail();

            if (!credentialsService.existsByUsername(email) && !userService.existsByEmailAndProvider(email, "google")) {
                // Salva l'utente nelle credenziali e negli utenti OAuth
                userService.saveOauthUser(oidcUser);
                User utenteOauth = userService.getCustomOAuth2UserByEmail(email);
                credentialsService.saveCredentialsFromOidcUser(utenteOauth);
            }

            return "redirect:/";  // Gli utenti OIDC sono sempre reindirizzati alla home
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Estrai le informazioni necessarie da userDetails
            String username = userDetails.getUsername();

            // Recupera le credenziali per determinare il ruolo dell'utente
            Credentials credentials = credentialsService.getCredentials(username);
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "admin/indexAdmin.html"; // Reindirizza gli amministratori alla pagina admin
            }
            return "redirect:/";  // Reindirizza gli altri utenti alla pagina principale
        }
        return "redirect:/"; // Redirezione di default
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }
	
    
    @GetMapping("/user")
    public String userProfile(Model model) {
        User user = globalController.getCurrentUser();

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("user_id", user.getId());
            return "userProfile";
        } else {
            return "redirect:/login";
        }
    }


    
    
	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult,
                 Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            if (credentialsService.usernameExists(credentials.getUsername())) {
                model.addAttribute("usernameError", "Username already exists. Please choose another one.");
                return "formRegisterUser";
            }
            userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "registerUser";
    }
	

}