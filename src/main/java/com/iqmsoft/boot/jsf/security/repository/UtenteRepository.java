package com.iqmsoft.boot.jsf.security.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iqmsoft.boot.jsf.security.domain.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
	
	List<Utente> findByUsername(String username);
	
	List<Utente> findByEmail(String email);

}
