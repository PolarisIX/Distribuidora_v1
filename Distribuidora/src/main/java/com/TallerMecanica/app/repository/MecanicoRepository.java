package com.TallerMecanica.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.TallerMecanica.app.model.Mecanico;

public interface MecanicoRepository extends MongoRepository<Mecanico, String> {
    // Consulta personalizada para buscar por cédula
    Mecanico findByCedula(String cedula);
    }
