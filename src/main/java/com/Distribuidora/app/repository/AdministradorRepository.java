package com.Distribuidora.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Distribuidora.app.model.Administrador;

public interface AdministradorRepository extends MongoRepository<Administrador, String> {
	Administrador findByCedula(String cedula);
	}
