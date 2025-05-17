package com.TallerMecanica.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.TallerMecanica.app.model.Repuesto;

public interface RepuestoRepository extends MongoRepository<Repuesto, String> {

	 Repuesto findByNombre(String nombre);
}