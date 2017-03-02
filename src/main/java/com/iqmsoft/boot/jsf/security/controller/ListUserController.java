package com.iqmsoft.boot.jsf.security.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqmsoft.boot.jsf.security.domain.Utente;
import com.iqmsoft.boot.jsf.security.service.RegistrazioneService;

@ManagedBean
@ViewScoped
public class ListUserController {
	
	private static final Logger log = LoggerFactory.getLogger(ListUserController.class);
	
	private List<Utente> listaUtenti;
	
	@ManagedProperty(value = "#{registrazioneService}")
    private RegistrazioneService registrazioneService;  
	
	
	public List<Utente> getListaUtenti() {
        if (this.listaUtenti == null) {
            this.listaUtenti = registrazioneService.listaUtenti();
        }
        return listaUtenti;
    }

	public RegistrazioneService getRegistrazioneService() {
		return registrazioneService;
	}

	public void setRegistrazioneService(RegistrazioneService registrazioneService) {
		this.registrazioneService = registrazioneService;
	}
	
	
	
	

}
