package com.TallerMecanica.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.TallerMecanica.app.model.Factura;

@Repository
public interface FacturaRepository extends MongoRepository<Factura, String> {
    // Aquí puedes agregar métodos personalizados si necesitas consultas específicas
}