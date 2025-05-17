package com.TallerMecanica.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.TallerMecanica.app.model.Administrador;

public interface AdministradorRepository extends MongoRepository<Administrador, String> {
	Administrador findByCedula(String cedula);
	}
