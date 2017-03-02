package com.iqmsoft.boot.jsf.security.controller;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.iqmsoft.boot.jsf.security.domain.Utente;
import com.iqmsoft.boot.jsf.security.service.RegistrazioneService;

@ManagedBean(name="registrazioneController")
@ViewScoped
public class RegistrazioneController {
	
	private static final Logger log = LoggerFactory.getLogger(RegistrazioneController.class);

	private RegistrazioneForm registrazioneForm = new RegistrazioneForm();
	
	@ManagedProperty(value = "#{registrazioneService}")
    private RegistrazioneService registrazioneService;  
	
	
	public String registraUtente() {
		
		log.info("Init Registration User...");
				
		if (paginaValida() == false) {
			return null;
		}

		// si salva nel db il nuovo utente
		Utente utente = new Utente();
		utente.setUsername(registrazioneForm.getUsername());
		utente.setPassword(new BCryptPasswordEncoder().encode(registrazioneForm.getPassword()));
		utente.setEmail(registrazioneForm.getEmail());
		utente.setDataCreazione(new Date());
		
		// di default si assegna all'utente il ruolo ROLE_USER
		utente.setRole(Utente.Role.ROLE_USER);
		
		registrazioneService.registraUtente(utente);
		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, null,
				"Username Registered");
		context.addMessage(null, message);
		
		cancellaForm();
		
		return null;
	}
	
	public void verificaEsistenzaUsername() {
		log.info("Init Verify Username...");
		boolean usernamePresente = registrazioneService.usernamePresente(registrazioneForm.getUsername());
		if (usernamePresente) {
			FacesContext.getCurrentInstance().addMessage("registrazione:username", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, 
							"Provide Alternate Username"));
		}
	}
	
	public void verificaRipetizionePassword() {
		log.info("init verify confirm password...");
		
		boolean ripetizionePasswordEsatta = registrazioneForm.getPassword().equals(registrazioneForm.getRepassword());
		if (!ripetizionePasswordEsatta) {
			FacesContext.getCurrentInstance().addMessage("registrazione:repassword", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Passwords Should be Same"));
		}
	}
	
	public void verificaMail() {
		
		// si verifica che la mail inserita sia valida
		if (!verificaValiditaEmail()) {
			return;
		}
		
		// si verifica che la mail inserita non sia gia' presente nel database
		verificaEsistenzaEmail();
		
	}
	
	public boolean verificaEsistenzaEmail() {
		log.info("verify email exists...");
		boolean emailPresente = registrazioneService.emailPresente(registrazioneForm.getEmail());
		if (emailPresente) {
			FacesContext.getCurrentInstance().addMessage("registrazione:email", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, 
							"Email Already Present"));
		}
		
		return emailPresente;
	}
	
	public boolean verificaValiditaEmail() {
		log.info("verifica validita' campo email...");
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		
		Pattern pattern = Pattern.compile(regex);
		String email = registrazioneForm.getEmail();
		Matcher matcher = pattern.matcher(email);
		
		boolean mailValida = matcher.matches();
		
		if (!mailValida) {
			FacesContext.getCurrentInstance().addMessage("registrazione:email", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Invalid Email"));
		}
		
		return mailValida;
	}
	
	
	
	private boolean paginaValida() {
		
		// validazione username
		boolean usernamePresente = registrazioneService.usernamePresente(registrazioneForm.getUsername());
		if (usernamePresente) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, 
					"Give a Alternate UserName");
            context.addMessage("registrazione:username", message);
            return false;
		}
		
		// validazione password
		if (!registrazioneForm.getPassword().equals(registrazioneForm.getRepassword())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Passwords not Same");
            context.addMessage("registrazione:repassword", message);
            return false;
		}
		
		// validazione e-mail - il campo email non deve essere vuoto
		if (registrazioneForm.getEmail() == null || registrazioneForm.getEmail().equals("")) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new 
					FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Invalid Email");
            context.addMessage("registrazione:email", message);
            return false;
		}
		
		// validazione e-mail - la mail deve essere valida
		if (!verificaValiditaEmail()) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					null, "Invalid Email");
            context.addMessage("registrazione:email", message);
			return false;
		}
		
		// validazione e-mail - la mail non deve essere gia' presente nel database
		boolean emailPresente = registrazioneService.emailPresente(registrazioneForm.getEmail());
		if (emailPresente) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					null, "Provide Alternate Email");
            context.addMessage("registrazione:email", message);
            return false;
		}
		
		// la pagina e' valida
		return true;
	}
	
	private void cancellaForm() {
		registrazioneForm.setEmail(null);
		registrazioneForm.setPassword(null);
		registrazioneForm.setUsername(null);
		registrazioneForm.setRepassword(null);
	}

	public RegistrazioneForm getRegistrazioneForm() {
		return registrazioneForm;
	}

	public void setRegistrazioneForm(RegistrazioneForm registrazioneForm) {
		this.registrazioneForm = registrazioneForm;
	}

	public RegistrazioneService getRegistrazioneService() {
		return registrazioneService;
	}

	public void setRegistrazioneService(RegistrazioneService registrazioneService) {
		this.registrazioneService = registrazioneService;
	}
	
	
}
