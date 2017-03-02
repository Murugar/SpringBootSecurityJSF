package com.iqmsoft.boot.jsf.security.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.iqmsoft.boot.jsf.security.controller.RegistrazioneController;
import com.iqmsoft.boot.jsf.security.domain.Utente;
import com.iqmsoft.boot.jsf.security.repository.UtenteRepository;

@Service
public class RegistrazioneService {
	
	private static final Logger log = LoggerFactory.getLogger(RegistrazioneController.class);
	
	@Autowired
	UtenteRepository utenteRepository;
	
	@Transactional
	public void registraUtente(Utente utente) {	
		log.info("RegistrazioneService: registraUtente");
		utenteRepository.save(utente);
	}
	
	public boolean usernamePresente(String username) {
		List<Utente> listaUtenti = utenteRepository.findByUsername(username);
		return listaUtenti.size() > 0 ? true : false;
	}
	
	public boolean emailPresente(String email) {
		List<Utente> listaUtenti = utenteRepository.findByEmail(email);
		return listaUtenti.size() > 0 ? true : false;
	}
	
	@Transactional
	public List<Utente> listaUtenti() {
		 List<Utente> listaUtenti =  Lists.newArrayList(utenteRepository.findAll());
		 return listaUtenti;
	}

}
