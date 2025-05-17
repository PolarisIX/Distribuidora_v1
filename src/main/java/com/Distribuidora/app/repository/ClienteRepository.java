package com.Distribuidora.app.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.Distribuidora.app.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String> {

    // Método para buscar un Cliente por su cédula
    Cliente findByCedula(String cedula);

    // Método para eliminar un Cliente por su cédula
    void deleteByCedula(String cedula);
    
    Cliente findByNombre(String nombre); 
    
}
