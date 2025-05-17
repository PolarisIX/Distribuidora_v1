package com.Distribuidora.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.Distribuidora.app.model.Repartidor;

public interface RepartidorRepository extends MongoRepository<Repartidor, String> {
    
    // Consulta personalizada para buscar un repartidor por su correo
    Repartidor findByCorreo(String correo);

    // Consulta personalizada para buscar un repartidor por su cédula
    Repartidor findByCedula(String cedula);
}
