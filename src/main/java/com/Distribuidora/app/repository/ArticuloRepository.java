package com.Distribuidora.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.Distribuidora.app.model.Articulo;
import java.util.Optional;

public interface ArticuloRepository extends MongoRepository<Articulo, String> {
    // Método para encontrar un artículo por nombre
    Articulo findByNombre(String nombre);

  
    Optional<Articulo> findById(String id);
}
