package com.iqmsoft.boot.jsf.security.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.iqmsoft.boot.jsf.security.domain.Utente;
import com.iqmsoft.boot.jsf.security.repository.UtenteRepository;

@Component
public class GestioneUtentiUserDetailsService implements UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(GestioneUtentiUserDetailsService.class);
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("username: " + username);
		log.info("inizio ricerca utenza in base dati...");
		
		List<Utente> listaUtenti = utenteRepository.findByUsername(username);
		
		if(listaUtenti == null || listaUtenti.size() == 0) {
			throw new UsernameNotFoundException(String.format("L'utente con username %s non esiste", username));
		}
		
		log.info("utente presente in base dati");
		
		// si legge l'unico utente presente in base dati (la username e' unica)
		Utente utente = listaUtenti.get(0);
	
		// Si crea la lista dei ruoli dell'utente (uno dei due ruoli gestiti)
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities = AuthorityUtils.createAuthorityList(utente.getRole().toString());
		
		// Create a UserDetails object from the data
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(utente.getUsername(), utente.getPassword(), authorities);
		return userDetails;
	}
}

