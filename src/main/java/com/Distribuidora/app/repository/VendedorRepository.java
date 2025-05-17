package com.Distribuidora.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.Distribuidora.app.model.Vendedor;

public interface VendedorRepository extends MongoRepository<Vendedor, String> {
    // Consulta personalizada para buscar por email
    Vendedor findByEmail(String email);
    
    Vendedor findByCedula(String cedula);
}
